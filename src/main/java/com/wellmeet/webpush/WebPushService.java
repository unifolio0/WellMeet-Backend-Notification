package com.wellmeet.webpush;

import com.wellmeet.exception.ErrorCode;
import com.wellmeet.exception.WellMeetNotificationException;
import com.wellmeet.webpush.domain.PushSubscription;
import com.wellmeet.webpush.dto.SubscribeRequest;
import com.wellmeet.webpush.dto.SubscribeResponse;
import com.wellmeet.webpush.dto.TestPushRequest;
import com.wellmeet.webpush.dto.UnsubscribeRequest;
import com.wellmeet.webpush.infrastructure.WebPushSender;
import com.wellmeet.webpush.repository.PushSubscriptionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WebPushService {

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final WebPushSender pushService;

    @Transactional
    public SubscribeResponse subscribe(String userId, SubscribeRequest request) {
        List<PushSubscription> existingSubscriptions = pushSubscriptionRepository.findByUserId(userId);
        Optional<PushSubscription> pushSubscription = existingSubscriptions.stream()
                .filter(subscription -> subscription.isSameEndpoint(request.endpoint()))
                .findAny();
        if (pushSubscription.isPresent()) {
            PushSubscription subscription = pushSubscription.get();
            subscription.update(request.toDomain(userId));
            return new SubscribeResponse(subscription);
        }
        PushSubscription subscription = request.toDomain(userId);
        PushSubscription savedSubscription = pushSubscriptionRepository.save(subscription);
        return new SubscribeResponse(savedSubscription);
    }

    public void sendTestPush(String userId, TestPushRequest request) {
        List<PushSubscription> subscriptions = pushSubscriptionRepository.findByUserId(userId);
        if (subscriptions.isEmpty()) {
            throw new WellMeetNotificationException(ErrorCode.SUBSCRIPTION_NOT_FOUND);
        }

        subscriptions.forEach(subscription -> pushService.send(subscription, request));
    }

    @Transactional
    public void unsubscribe(String userId, UnsubscribeRequest request) {
        if (!pushSubscriptionRepository.existsByUserIdAndEndpoint(userId, request.endpoint())) {
            throw new WellMeetNotificationException(ErrorCode.SUBSCRIPTION_NOT_FOUND);
        }
        pushSubscriptionRepository.deleteByUserIdAndEndpoint(userId, request.endpoint());
    }
}
