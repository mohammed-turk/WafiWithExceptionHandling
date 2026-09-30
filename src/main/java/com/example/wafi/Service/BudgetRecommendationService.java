package com.example.wafi.Service;

import com.example.wafi.API.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetRecommendationService {
    private final AIService aiService;

    public String recommend(Double budget, String category) {

        if (budget.isNaN() || budget.isInfinite() || budget <= 0)
            throw new APIException("budget must be a positive number");

        String normalized = category.toLowerCase();
        if (!normalized.equals("data") && !normalized.equals("minutes") && !normalized.equals("compo"))
            throw new APIException("category must be data, minutes, or compo");

        String prompt = String.format(
                "A user in Saudi Arabia wants a telecom plan under %.2f SAR per month, " +
                        "in the %s category (data, minutes, or compo). Suggest one specific real plan " +
                        "and provider (STC, Mobily, Zain, or Virgin Mobile) that fits this budget. " +
                        "Reply in 2-3 short sentences, no markdown.",
                budget, normalized
        );

        String recommendation = aiService.callAi(prompt);


        if (recommendation == null || recommendation.isBlank())
            throw new APIException("AI service returned no response, try again later");

        return recommendation;
    }
}