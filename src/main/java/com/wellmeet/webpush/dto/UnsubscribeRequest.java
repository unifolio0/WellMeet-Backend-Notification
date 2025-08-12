package com.wellmeet.webpush.dto;

import jakarta.validation.constraints.NotBlank;

public record UnsubscribeRequest(
        @NotBlank
        String endpoint
) {
}
