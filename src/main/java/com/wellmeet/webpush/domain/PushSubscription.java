package com.wellmeet.webpush.domain;

import com.wellmeet.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushSubscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String endpoint;

    @NotNull
    private String p256dh;

    @NotNull
    private String auth;

    private boolean active;

    public PushSubscription(String userId, String endpoint, String p256dh, String auth) {
        this.userId = userId;
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        this.auth = auth;
        this.active = true;
    }

    public boolean isSameEnpPoint(String endpoint) {
        return this.endpoint.equals(endpoint);
    }
}
