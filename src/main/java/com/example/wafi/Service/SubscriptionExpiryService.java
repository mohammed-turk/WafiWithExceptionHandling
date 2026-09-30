package com.example.wafi.Service;

import com.example.wafi.API.APIException;
import com.example.wafi.Model.Subscriptions;
import com.example.wafi.Model.Users;
import com.example.wafi.Repository.SubscriptionsRepository;
import com.example.wafi.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionExpiryService {
    private final SubscriptionsRepository subscriptionsRepository;
    private final UsersRepository usersRepository;
    private final SubscriptionsService subscriptionsService;
    private final AIService aiService;
    private final WhatsAppService whatsAppService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *")
    public void checkExpiringSubscriptions() {
        List<Subscriptions> activeSubs = subscriptionsRepository.findSubscriptionsByStatus("ACTIVE");

        LocalDate today = LocalDate.now();

        for (Subscriptions sub : activeSubs) {
            //    not stop the rest of the loop on failed subscription (ai, email, db)
            try {
                LocalDate endDate = subscriptionsService.calculateEndDate(sub);
                long daysLeft = ChronoUnit.DAYS.between(today, endDate);

                if (daysLeft < 0) {
                    sub.setStatus("EXPIRED");
                    subscriptionsRepository.save(sub);
                    continue;
                }

                if (daysLeft <= 3 && !Boolean.TRUE.equals(sub.getSent())) {
                    Users user = usersRepository.findUsersById(sub.getUserId());
                    if (user != null)
                        notifyUser(sub, user, daysLeft);
                }
            } catch (Exception e) {
                log.error("expiry check failed for subscription {}: {}", sub.getId(), e.getMessage());
            }
        }
    }

    private void notifyUser(Subscriptions sub, Users user, long daysLeft) {
        String prompt = String.format(
                "A telecom user in Saudi Arabia has a %s plan from %s costing %.2f SAR/%s (category: %s). " +
                        "Their plan expires in %d day(s). Suggest one competing plan from another Saudi provider " +
                        "(STC, Mobily, Zain, or Virgin Mobile) that offers similar or better value. " +
                        "Reply in 2-3 short sentences, no markdown.",
                sub.getPlanName(), sub.getProviderName(), sub.getCharge(), sub.getPeriod(),
                sub.getCategory(), daysLeft
        );

        String aiSuggestion = aiService.callAi(prompt);

        String message = String.format(
                "Hi %s, your %s (%s) plan expires in %d day(s). %s",
                user.getName(), sub.getPlanName(), sub.getProviderName(), daysLeft, aiSuggestion
        );

        trySendWhatsApp(user.getPhoneNumber(), message);
        emailService.sendEmail(user.getEmail(), "Your plan is expiring soon", message);

        sub.setAiSuggestion(aiSuggestion);
        sub.setMessage(message);
        sub.setSent(true);
        subscriptionsRepository.save(sub);
    }

    public String forceNotify(Integer subscriptionId) {
        Subscriptions sub = subscriptionsRepository.findSubscriptionsById(subscriptionId);
        if (sub == null)
            throw new APIException("subscription not found");

        Users user = usersRepository.findUsersById(sub.getUserId());
        if (user == null)
            throw new APIException("user not found");

        LocalDate endDate = subscriptionsService.calculateEndDate(sub);
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), endDate);

        //    to tell the user "expires in -x days"
        if (daysLeft < 0)
            throw new APIException("subscription already expired");

        notifyUser(sub, user, daysLeft);
        return sub.getAiSuggestion();
    }

    private boolean trySendWhatsApp(String phoneNumber, String message) {
        try {
            String formatted = "966" + phoneNumber.substring(1);
            whatsAppService.sendWhatsApp(formatted, message);
            return true;
        } catch (Exception e) {
            log.warn("whatsapp failed for {}: {}", phoneNumber, e.getMessage());
            return false;
        }
    }
}