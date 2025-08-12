package com.wellmeet.webpush;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wellmeet.BaseControllerTest;
import com.wellmeet.fixture.NullAndEmptyAndBlankSource;
import com.wellmeet.webpush.domain.PushSubscription;
import com.wellmeet.webpush.dto.SubscribeRequest;
import com.wellmeet.webpush.dto.SubscribeResponse;
import com.wellmeet.webpush.dto.TestPushRequest;
import com.wellmeet.webpush.dto.UnsubscribeRequest;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.http.HttpStatus;

class WebPushControllerTest extends BaseControllerTest {

    @Nested
    class Subscribe {

        @Test
        void 웹_푸시를_구독할_수_있다() {
            SubscribeRequest request = new SubscribeRequest("endpoint", "p256dh", "auth");
            String userId = "1";

            SubscribeResponse response = given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", userId)
                    .body(request)
                    .when().post("/notification/subscribe")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract().as(SubscribeResponse.class);

            assertAll(
                    () -> assertThat(response.userId()).isEqualTo(userId),
                    () -> assertThat(response.endpoint()).isEqualTo(request.endpoint()),
                    () -> assertThat(response.p256dh()).isEqualTo(request.p256dh()),
                    () -> assertThat(response.auth()).isEqualTo(request.auth())
            );
        }

        @ParameterizedTest
        @NullAndEmptyAndBlankSource
        void endpoint는_개행글자_외_다른_글자가_포함되야_한다(String endpoint) {
            SubscribeRequest request = new SubscribeRequest(endpoint, "p256dh", "auth");

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", "1")
                    .body(request)
                    .when().post("/notification/subscribe")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @ParameterizedTest
        @NullAndEmptyAndBlankSource
        void p256dh는_개행글자_외_다른_글자가_포함되야_한다(String p256dh) {
            SubscribeRequest request = new SubscribeRequest("endpoint", p256dh, "auth");

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", "1")
                    .body(request)
                    .when().post("/notification/subscribe")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @ParameterizedTest
        @NullAndEmptyAndBlankSource
        void auth는_개행글자_외_다른_글자가_포함되야_한다(String auth) {
            SubscribeRequest request = new SubscribeRequest("endpoint", "p256dh", auth);
            given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", "1")
                    .body(request)
                    .when().post("/notification/subscribe")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class TestPush {

        @ParameterizedTest
        @NullAndEmptyAndBlankSource
        void title은_개행글자_외_다른_글자가_포함되야_한다(String title) {
            String userId = "1";
            String body = "Test Body";
            Map<String, Object> data = Map.of("key", "value");

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", userId)
                    .body(new TestPushRequest(title, body, data))
                    .when().post("/notification/test-push")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @ParameterizedTest
        @NullAndEmptyAndBlankSource
        void body는_개행글자_외_다른_글자가_포함되야_한다(String body) {
            String userId = "1";
            String title = "Test Title";
            Map<String, Object> data = Map.of("key", "value");

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", userId)
                    .body(new TestPushRequest(title, body, data))
                    .when().post("/notification/test-push")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void data는_null이_아니어야_한다() {
            String userId = "1";
            String title = "Test Title";
            String body = "Test Body";

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", userId)
                    .body(new TestPushRequest(title, body, null))
                    .when().post("/notification/test-push")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class Unsubscribe {

        @Test
        void 웹_푸시를_구독취소할_수_있다() {
            UnsubscribeRequest request = new UnsubscribeRequest("endpoint");
            String userId = "1";
            PushSubscription pushSubscription = new PushSubscription(userId, request.endpoint(), "p256dh", "auth");
            pushSubscriptionRepository.save(pushSubscription);

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", userId)
                    .body(request)
                    .when().delete("/notification/unsubscribe")
                    .then().statusCode(HttpStatus.NO_CONTENT.value());
        }

        @ParameterizedTest
        @NullAndEmptyAndBlankSource
        void endpoint는_개행글자_외_다른_글자가_포함되야_한다(String endpoint) {
            UnsubscribeRequest request = new UnsubscribeRequest(endpoint);

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", "1")
                    .body(request)
                    .when().delete("/notification/unsubscribe")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
