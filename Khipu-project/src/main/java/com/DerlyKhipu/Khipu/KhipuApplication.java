package com.DerlyKhipu.Khipu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableRetry
@EnableAsync
@SpringBootApplication
public class KhipuApplication {
    public static void main(String[] args) {
        SpringApplication.run(KhipuApplication.class, args);
    }
}

// Derly Ortiz Ubiera