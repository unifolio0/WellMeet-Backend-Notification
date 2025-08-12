package com.wellmeet.webpush.dto;

import com.wellmeet.webpush.domain.PushSubscription;

public record SubscribeResponse(
        Long subscriptionId,
        String userId,
        String endpoint,
        String p256dh,
        String auth
) {

    public SubscribeResponse(PushSubscription subscription) {
        this(
                subscription.getId(),
                subscription.getUserId(),
                subscription.getEndpoint(),
                subscription.getP256dh(),
                subscription.getAuth()
        );
    }
}
