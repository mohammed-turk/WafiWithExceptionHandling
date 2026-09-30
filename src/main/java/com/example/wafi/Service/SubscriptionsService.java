package com.example.wafi.Service;

import com.example.wafi.API.APIException;
import com.example.wafi.Model.Subscriptions;
import com.example.wafi.Repository.SubscriptionsRepository;
import com.example.wafi.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionsService {
    private final SubscriptionsRepository subscriptionsRepository;
    private final UsersRepository usersRepository;

    private Subscriptions getSubscriptionOrThrow(Integer subscriptionId){
        Subscriptions subscription = subscriptionsRepository.findSubscriptionsById(subscriptionId);
        if (subscription == null)
            throw new APIException("subscription not found");
        return subscription;
    }

    private void checkUserExists(Integer userId){
        if (usersRepository.findUsersById(userId) == null)
            throw new APIException("user not found");
    }
    public List<Subscriptions> get(){
        return subscriptionsRepository.findAll();
    }

    public void add(Subscriptions subscription){
        if (subscription.getId() != null)
            throw new APIException("id is auto generated, do not send it");
        checkUserExists(subscription.getUserId());
        subscription.setStatus("ACTIVE");
        subscription.setSent(false);
        subscriptionsRepository.save(subscription);
    }

    public void update(Integer subscriptionId, Subscriptions subscription){
        Subscriptions oldSubscription = getSubscriptionOrThrow(subscriptionId);

        oldSubscription.setAutoRenew(subscription.getAutoRenew());
        oldSubscription.setCategory(subscription.getCategory());
        oldSubscription.setCharge(subscription.getCharge());
        oldSubscription.setDataAllowanceGb(subscription.getDataAllowanceGb());
        oldSubscription.setMinutesAllowance(subscription.getMinutesAllowance());
        oldSubscription.setPeriod(subscription.getPeriod());
        oldSubscription.setPlanName(subscription.getPlanName());
        oldSubscription.setProviderName(subscription.getProviderName());
        oldSubscription.setSmsAllowance(subscription.getSmsAllowance());
        oldSubscription.setStartDate(subscription.getStartDate());
        subscriptionsRepository.save(oldSubscription);

    }

    public void delete(Integer subscriptionId){
        Subscriptions desiredSubscription = getSubscriptionOrThrow(subscriptionId);
        subscriptionsRepository.delete(desiredSubscription);
    }

    public List<Subscriptions> getUserSubscriptions(Integer userId) {
        checkUserExists(userId);
        return subscriptionsRepository.findSubscriptionsByUserId(userId);
    }

    public Double getTotalSpend(Integer userId) {
        checkUserExists(userId);
        List<Subscriptions> activeSubs = subscriptionsRepository.findSubscriptionsByUserIdAndStatus(userId, "ACTIVE");
        double total = 0;
        for (Subscriptions s : activeSubs) {
            if (s.getCharge() != null) total += s.getCharge();
        }
        return total;
    }

    public Boolean activate(Integer subscriptionId) {
        Subscriptions subscription = getSubscriptionOrThrow(subscriptionId);
        subscription.setStatus("ACTIVE");
        subscriptionsRepository.save(subscription);
        return true;
    }

    public Boolean expire(Integer subscriptionId) {
        Subscriptions subscription = getSubscriptionOrThrow(subscriptionId);

        subscription.setStatus("EXPIRED");
        subscriptionsRepository.save(subscription);
        return true;
    }

    public List<Subscriptions> getByCategory(String category){
        String normalized = category.toLowerCase();
        if (!normalized.equals("data") && !normalized.equals("minutes") && !normalized.equals("compo"))
            throw new APIException("category must be data, minutes, or compo");

        return subscriptionsRepository.findSubscriptionsByCategory(normalized);
    }

    public LocalDate calculateEndDate(Subscriptions sub) {
        LocalDate start = sub.getStartDate();
        return switch (sub.getPeriod()) {
            case "DAY" -> start.plusDays(1);
            case "WEEK" -> start.plusWeeks(1);
            case "MONTH" -> start.plusMonths(1);
            case "THREE_MONTHS" -> start.plusMonths(3);
            case "SIX_MONTHS" -> start.plusMonths(6);
            case "YEAR" -> start.plusYears(1);
            default -> start;
        };
    }
}