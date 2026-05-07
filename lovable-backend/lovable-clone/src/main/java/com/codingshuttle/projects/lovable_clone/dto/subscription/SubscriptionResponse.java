package com.codingshuttle.projects.lovable_clone.dto.subscription;

import java.time.Instant;

public record SubscriptionResponse(
        PlanResponse plan,
        String status,
        Instant currentPeriodEnd,
        Long tokensUsedThisCycle
) {
}
