package com.example.wafi.Controller;

import com.example.wafi.API.APIResponse;
import com.example.wafi.Service.BudgetRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/budget-recommendation")
@RequiredArgsConstructor
public class BudgetRecommendationController {
    private final BudgetRecommendationService budgetRecommendationService;

    @GetMapping("/{budget}/{category}")
    public ResponseEntity<?> recommend(@PathVariable Double budget, @PathVariable String category){
        return ResponseEntity.status(200).body(new APIResponse(budgetRecommendationService.recommend(budget, category)));
    }
}