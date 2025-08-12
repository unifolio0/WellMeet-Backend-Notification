package com.wellmeet.webpush.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellmeet.config.VapidConfig;
import com.wellmeet.exception.ErrorCode;
import com.wellmeet.exception.WellMeetNotificationException;
import com.wellmeet.webpush.domain.PushSubscription;
import com.wellmeet.webpush.dto.TestPushRequest;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.Subscription.Keys;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebPushSender {

    private final VapidConfig vapidConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private PushService pushService;

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
        try {
            pushService = new PushService();
            pushService.setPublicKey(vapidConfig.getPublicKey());
            pushService.setPrivateKey(vapidConfig.getPrivateKey());
            pushService.setSubject(vapidConfig.getSubject());
        } catch (Exception e) {
            throw new WellMeetNotificationException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void send(PushSubscription subscription, TestPushRequest request) {
        Keys keys = new Keys(subscription.getP256dh(), subscription.getAuth());
        Subscription sub = new Subscription(subscription.getEndpoint(), keys);
        Map<String, Object> notificationPayload = getNotificationPayload(request);
        webPushSend(notificationPayload, sub);
    }

    private Map<String, Object> getNotificationPayload(TestPushRequest request) {
        Map<String, Object> notificationPayload = new HashMap<>();
        notificationPayload.put("title", request.title());
        notificationPayload.put("body", request.body());
        notificationPayload.put("icon", "/icon-192x192.png");
        notificationPayload.put("badge", "/badge-72x72.png");
        notificationPayload.put("vibrate", new int[]{100, 50, 100});
        notificationPayload.put("requireInteraction", false);

        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("url", "/notifications");
        defaultData.put("timestamp", System.currentTimeMillis());
        notificationPayload.put("data", defaultData);
        return notificationPayload;
    }

    private void webPushSend(Map<String, Object> notificationPayload, Subscription sub) {
        try {
            String payloadJson = objectMapper.writeValueAsString(notificationPayload);
            Notification notification = new Notification(sub, payloadJson);
            pushService.send(notification);
        } catch (JoseException | GeneralSecurityException | IOException | ExecutionException | InterruptedException e) {
            throw new WellMeetNotificationException(ErrorCode.WEB_PUSH_SEND_FAILED);
        }
    }
}
