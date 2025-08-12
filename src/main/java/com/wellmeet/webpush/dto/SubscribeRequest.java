package com.wellmeet.webpush.dto;

import com.wellmeet.webpush.domain.PushSubscription;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record SubscribeRequest(
        @NotBlank
        String endpoint,

        @Valid
        Keys keys
) {

    public SubscribeRequest(String endpoint, String p256dh, String auth) {
        this(endpoint, new Keys(p256dh, auth));
    }

    public PushSubscription toDomain(String userId) {
        return new PushSubscription(
                userId,
                endpoint,
                keys.p256dh(),
                keys.auth()
        );
    }

    public String p256dh() {
        return keys.p256dh();
    }

    public String auth() {
        return keys.auth();
    }

    public record Keys(
            @NotBlank
            String p256dh,

            @NotBlank
            String auth
    ) {
    }
}
