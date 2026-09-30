package com.example.wafi.Service;

import com.example.wafi.API.APIException;
import com.example.wafi.API.APIResponse;
import com.example.wafi.Model.IssueReports;
import com.example.wafi.Model.Subscriptions;
import com.example.wafi.Model.Users;
import com.example.wafi.Repository.IssueReportsRepository;
import com.example.wafi.Repository.UsersRepository;
import com.example.wafi.Repository.SubscriptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IssueReportsService {
    private final IssueReportsRepository issueReportsRepository;
    private final UsersRepository usersRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final AIService aiService;

    private IssueReports getIssueReportOrThrow(Integer issueReportId){
        IssueReports issueReport = issueReportsRepository.findIssueReportsById(issueReportId);
        if (issueReport == null)
            throw new APIException("issue report not found");
        return issueReport;
    }

    public List<IssueReports> get(){
        return issueReportsRepository.findAll();
    }

    public IssueReports getOne(Integer issueReportId){
        return issueReportsRepository.findIssueReportsById(issueReportId);
    }

    public void add(IssueReports issueReport){
        if (issueReport.getId() != null)
            throw new APIException("id is auto generated, do not send it");

        if (usersRepository.findUsersById(issueReport.getUserId()) == null)
            throw new APIException("no such user exist");

        Subscriptions subscription = subscriptionsRepository.findSubscriptionsById(issueReport.getSubscriptionId());
        if (subscription == null)
            throw new APIException("subscription not found");

        if (!subscription.getUserId().equals(issueReport.getUserId()))
            throw new APIException("subscription does not belong to this user");

        issueReport.setStatus("OPEN");
        issueReport.setAiResponse(null);
        issueReportsRepository.save(issueReport);
    }

    public void update(Integer issueReportId, IssueReports issueReport){
        IssueReports oldIssueReport = getIssueReportOrThrow(issueReportId);

        oldIssueReport.setDescription(issueReport.getDescription());
        issueReportsRepository.save(oldIssueReport);
    }

    public void delete(Integer issueReportId){
        IssueReports issueReport = getIssueReportOrThrow(issueReportId);

        issueReportsRepository.delete(issueReport);

    }

    public void resolveIssue(Integer issueReportId) {
        IssueReports issue = getIssueReportOrThrow(issueReportId);
        issue.setStatus("RESOLVED");
        issueReportsRepository.save(issue);
    }

    public List<IssueReports> getByStatus(String status){
        String normalized = status.toUpperCase();
        if (!normalized.equals("OPEN") && !normalized.equals("RESOLVED"))
            throw new APIException("status must be OPEN or RESOLVED");

        return issueReportsRepository.findIssueReportsByStatus(normalized);
    }

    public List<IssueReports> getBySubscription(Integer subscriptionId){
        if (subscriptionsRepository.findSubscriptionsById(subscriptionId) == null)
            throw new APIException("subscription not found");

        return issueReportsRepository.findIssueReportsBySubscriptionId(subscriptionId);
         }

    public List<Map<String, Object>> getOpenCountPerUser() {
        List<IssueReports> openIssues = issueReportsRepository.findIssueReportsByStatus("OPEN");
        Map<Integer, Long> counts = new HashMap<>();
        for (IssueReports i : openIssues) {
            counts.merge(i.getUserId(), 1L, Long::sum);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : counts.entrySet()) {
            Users user = usersRepository.findUsersById(entry.getKey());
            Map<String, Object> row = new HashMap<>();
            row.put("userId", entry.getKey());
            row.put("userName", user != null ? user.getName() : "unknown");
            row.put("openIssueCount", entry.getValue());
            result.add(row);
        }
        return result;
    }

    public String resolveWithAi(Integer issueReportId) {
        IssueReports issue = getIssueReportOrThrow(issueReportId);

        Subscriptions sub = subscriptionsRepository.findSubscriptionsById(issue.getSubscriptionId());
        String planContext = sub != null
                ? String.format("%s plan from %s (%s category, status: %s)",
                sub.getPlanName(), sub.getProviderName(), sub.getCategory(), sub.getStatus())
                : "an unspecified plan";

        String prompt = String.format(
                "A telecom subscriber in Saudi Arabia reported this issue: \"%s\" " +
                        "(related to a %s). Give a short, practical troubleshooting suggestion " +
                        "or explanation in 2-3 sentences. If it likely needs human support, say so plainly. No markdown.",
                issue.getDescription(), planContext
        );

        String aiResponse = aiService.callAi(prompt);

        if (aiResponse == null || aiResponse.isBlank())
            throw new APIException("AI service returned no response, try again later");

        issue.setAiResponse(aiResponse);
        issueReportsRepository.save(issue);
        return aiResponse;
    }
}