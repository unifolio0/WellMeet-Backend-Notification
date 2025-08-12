package com.wellmeet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구독 정보를 찾을 수 없습니다."),

    FIELD_ERROR(HttpStatus.BAD_REQUEST, "입력이 잘못되었습니다."),
    URL_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "입력이 잘못되었습니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "입력한 값의 타입이 잘못되었습니다."),

    CORS_ORIGIN_STRING_BLANK(HttpStatus.INTERNAL_SERVER_ERROR, "CORS Origin 에 빈 값이 들어올 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    CORS_ORIGIN_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "CORS Origin 은 적어도 한 개 있어야 합니다"),
    WEB_PUSH_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "웹 푸시 전송에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
