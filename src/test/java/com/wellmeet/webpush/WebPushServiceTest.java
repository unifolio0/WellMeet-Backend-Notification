package com.wellmeet.webpush;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wellmeet.BaseServiceTest;
import com.wellmeet.exception.ErrorCode;
import com.wellmeet.exception.WellMeetNotificationException;
import com.wellmeet.webpush.domain.PushSubscription;
import com.wellmeet.webpush.dto.SubscribeRequest;
import com.wellmeet.webpush.dto.SubscribeResponse;
import com.wellmeet.webpush.dto.UnsubscribeRequest;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WebPushServiceTest extends BaseServiceTest {

    @Autowired
    private WebPushService webPushService;

    @Nested
    class Subscribe {

        @Test
        void 웹_푸시_알림을_구독한다() {
            String userId = UUID.randomUUID().toString();
            SubscribeRequest request = new SubscribeRequest("endpoint", "p256dh", "auth");

            SubscribeResponse response = webPushService.subscribe(userId, request);
            PushSubscription subscription = pushSubscriptionRepository.findById(response.subscriptionId()).get();

            assertAll(
                    () -> assertThat(subscription.getUserId()).isEqualTo(userId),
                    () -> assertThat(subscription.getEndpoint()).isEqualTo(request.endpoint()),
                    () -> assertThat(subscription.getP256dh()).isEqualTo(request.p256dh()),
                    () -> assertThat(subscription.getAuth()).isEqualTo(request.auth())
            );
        }

        @Test
        void 동일한_유저_아이디와_엔드포인트로_구독하면_기존_구독을_반환한다() {
            String userId = UUID.randomUUID().toString();
            String endpoint = "endpoint";
            PushSubscription pushSubscription = new PushSubscription(userId, endpoint, "p256dh", "auth");
            pushSubscriptionRepository.save(pushSubscription);
            SubscribeRequest request = new SubscribeRequest(endpoint, "change_p256dh", "change_auth");

            SubscribeResponse response = webPushService.subscribe(userId, request);
            PushSubscription subscription = pushSubscriptionRepository.findById(response.subscriptionId()).get();

            assertAll(
                    () -> assertThat(subscription.getUserId()).isEqualTo(userId),
                    () -> assertThat(subscription.getEndpoint()).isEqualTo(endpoint),
                    () -> assertThat(subscription.getP256dh()).isEqualTo(pushSubscription.getP256dh()),
                    () -> assertThat(subscription.getAuth()).isEqualTo(pushSubscription.getAuth())
            );
        }
    }

    @Nested
    class Unsubscribe {

        @Test
        void 웹_푸시_알림을_구독취소한다() {
            String userId = UUID.randomUUID().toString();
            String endpoint = "endpoint";
            PushSubscription pushSubscription = new PushSubscription(userId, endpoint, "p256dh", "auth");
            PushSubscription subscription = pushSubscriptionRepository.save(pushSubscription);

            webPushService.unsubscribe(userId, new UnsubscribeRequest(endpoint));
            Optional<PushSubscription> foundSubscription = pushSubscriptionRepository.findById(subscription.getId());

            assertThat(foundSubscription).isEmpty();
        }

        @Test
        void 존재하지_않는_구독을_취소하려고_하면_예외를_던진다() {
            String userId = UUID.randomUUID().toString();
            String endpoint = "nonexistent_endpoint";

            assertThatThrownBy(() -> webPushService.unsubscribe(userId, new UnsubscribeRequest(endpoint)))
                    .isInstanceOf(WellMeetNotificationException.class)
                    .hasMessageContaining(ErrorCode.SUBSCRIPTION_NOT_FOUND.getMessage());
        }
    }
}
