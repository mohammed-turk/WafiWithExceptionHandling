package com.example.wafi.Controller;

import com.example.wafi.API.APIResponse;
import com.example.wafi.Model.IssueReports;
import com.example.wafi.Service.IssueReportsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/issue-report")
@RequiredArgsConstructor
public class IssueReportsController {
    private final IssueReportsService issueReportsService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        return ResponseEntity.status(200).body(issueReportsService.get());
    }

    @GetMapping("/get/{issueReportId}")
    public ResponseEntity<?> getOne(@PathVariable Integer issueReportId){
        return ResponseEntity.status(200).body(issueReportsService.getOne(issueReportId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid IssueReports issueReport){
        issueReportsService.add(issueReport);
        return ResponseEntity.status(200).body(new APIResponse("issue report added successfully"));
    }

    @PutMapping("/update/{issueReportId}")
    public ResponseEntity<?> update(@PathVariable Integer issueReportId, @RequestBody @Valid IssueReports issueReport){
        issueReportsService.update(issueReportId, issueReport);
        return ResponseEntity.status(200).body(new APIResponse("issue report updated successfully"));
    }

    @DeleteMapping("/delete/{issueReportId}")
    public ResponseEntity<?> delete(@PathVariable Integer issueReportId){
        issueReportsService.delete(issueReportId);
        return ResponseEntity.status(200).body(new APIResponse("issue report deleted successfully"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable String status){
        return ResponseEntity.status(200).body(issueReportsService.getByStatus(status));
    }

    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<?> getBySubscription(@PathVariable Integer subscriptionId){
        return ResponseEntity.status(200).body(issueReportsService.getBySubscription(subscriptionId));
    }

    @PutMapping("/resolve/{issueReportId}")
    public ResponseEntity<?> resolve(@PathVariable Integer issueReportId){
        issueReportsService.resolveIssue(issueReportId);
        return ResponseEntity.status(200).body(new APIResponse("issue report resolved successfully"));
    }

    @GetMapping("/open-count")
    public ResponseEntity<?> getOpenCountPerUser(){
        return ResponseEntity.status(200).body(issueReportsService.getOpenCountPerUser());
    }

    @PostMapping("/resolve-ai/{issueReportId}")
    public ResponseEntity<?> resolveWithAi(@PathVariable Integer issueReportId){
        return ResponseEntity.status(200).body(new APIResponse(issueReportsService.resolveWithAi(issueReportId)));
    }
}