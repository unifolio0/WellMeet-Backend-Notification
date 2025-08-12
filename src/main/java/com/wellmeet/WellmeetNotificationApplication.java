package com.wellmeet;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class WellmeetNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellmeetNotificationApplication.class, args);
    }
}
