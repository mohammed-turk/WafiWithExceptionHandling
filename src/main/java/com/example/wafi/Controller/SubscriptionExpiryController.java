package com.example.wafi.Controller;

import com.example.wafi.API.APIResponse;
import com.example.wafi.Service.SubscriptionExpiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription-expiry")
@RequiredArgsConstructor
public class SubscriptionExpiryController {
    private final SubscriptionExpiryService subscriptionExpiryService;

    @PostMapping("/check")
    public ResponseEntity<?> checkNow() {
        subscriptionExpiryService.checkExpiringSubscriptions();
        return ResponseEntity.status(200).body(new APIResponse("expiry check completed"));
    }

    @PostMapping("/notify/{subscriptionId}")
    public ResponseEntity<?> forceNotify(@PathVariable Integer subscriptionId) {
        String suggestion = subscriptionExpiryService.forceNotify(subscriptionId);
        return ResponseEntity.status(200).body(new APIResponse("notification sent: " + suggestion));
    }
}