package com.wellmeet.webpush.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record TestPushRequest(
        @NotBlank
        String title,

        @NotBlank
        String body,

        @NotNull
        Map<String, Object> data
) {
}
