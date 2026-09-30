package com.example.wafi.Controller;

import com.example.wafi.API.APIResponse;
import com.example.wafi.Model.Subscriptions;
import com.example.wafi.Service.SubscriptionsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionsController {
    private final SubscriptionsService subscriptionsService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        List<Subscriptions> subscriptions = subscriptionsService.get();
        return ResponseEntity.status(200).body(subscriptions);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Subscriptions subscription){
        subscriptionsService.add(subscription);
        return ResponseEntity.status(200).body(new APIResponse("subscription added successfully"));
    }

    @PutMapping("/update/{subscriptionId}")
    public ResponseEntity<?> update(@PathVariable Integer subscriptionId, @RequestBody @Valid Subscriptions subscription){
        subscriptionsService.update(subscriptionId, subscription);
        return ResponseEntity.status(200).body(new APIResponse("subscription updated successfully"));
    }

    @DeleteMapping("/delete/{subscriptionId}")
    public ResponseEntity<?> delete(@PathVariable Integer subscriptionId){
        subscriptionsService.delete(subscriptionId);
        return ResponseEntity.status(200).body(new APIResponse("subscription deleted successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserSubscriptions(@PathVariable Integer userId){
        List<Subscriptions> subs = subscriptionsService.getUserSubscriptions(userId);
        return ResponseEntity.status(200).body(subs);
    }

    @GetMapping("/total-spend/{userId}")
    public ResponseEntity<?> getTotalSpend(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(subscriptionsService.getTotalSpend(userId));
    }

    @PutMapping("/activate/{subscriptionId}")
    public ResponseEntity<?> activate(@PathVariable Integer subscriptionId){
        subscriptionsService.activate(subscriptionId);
        return ResponseEntity.status(200).body(new APIResponse("subscription activated successfully"));
    }

    @PutMapping("/expire/{subscriptionId}")
    public ResponseEntity<?> expire(@PathVariable Integer subscriptionId){
        subscriptionsService.expire(subscriptionId);
        return ResponseEntity.status(200).body(new APIResponse("subscription expired successfully"));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getByCategory(@PathVariable String category){
        List<Subscriptions> subs = subscriptionsService.getByCategory(category);
        return ResponseEntity.status(200).body(subs);
    }
}