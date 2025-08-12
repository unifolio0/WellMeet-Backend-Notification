package com.wellmeet.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "vapid")
public class VapidConfig {
    private final String publicKey;
    private final String privateKey;
    private final String subject;
}
