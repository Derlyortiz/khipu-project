package com.DerlyKhipu.Khipu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "khipu")
public class KhipuConfig {
    private String apiUrl;
    private String apiKey;
    private Long receiverId;
    private String returnUrl;
    private String cancelUrl;
    private String webhookUrl;
}

// Derly Ortiz Ubiera