package com.wellmeet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WellMeetNotificationException extends RuntimeException {

    private final HttpStatus statusCode;

    public WellMeetNotificationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.statusCode = errorCode.getStatus();
    }
}
