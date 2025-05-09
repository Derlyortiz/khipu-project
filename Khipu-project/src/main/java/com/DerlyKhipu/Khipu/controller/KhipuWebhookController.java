package com.DerlyKhipu.Khipu.controller;

import com.DerlyKhipu.Khipu.config.KhipuConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import com.DerlyKhipu.Khipu.service.PaymentService;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/khipu/webhook")
@RequiredArgsConstructor
public class KhipuWebhookController {
    private final PaymentService paymentService;
    private final KhipuConfig khipuConfig;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
        @RequestBody WebhookNotification notification,
        @RequestHeader("x-api-key") String receivedApiKey,
        @RequestHeader("x-notification-signature") String signature
    ) {

        if (!khipuConfig.getApiKey().equals(receivedApiKey)) {
            log.warn("Intento de acceso no autorizado con API Key incorrecta");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String concatenated = String.format(
            "payment_id=%s&status=%s&notification_token=%s",
            notification.getPaymentId(),
            notification.getStatus(),
            notification.getNotificationToken()
        );
        
        String expectedSignature = DigestUtils.sha256Hex(concatenated + "&" + khipuConfig.getApiKey());
        
        if (!expectedSignature.equals(signature)) {
            log.error("Firma de notificación inválida para pago {}", notification.getPaymentId());
            return ResponseEntity.status(403).body("Invalid signature");
        }

        try {
            paymentService.processPaymentNotification(notification);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Error procesando notificación para pago {}", notification.getPaymentId(), e);
            return ResponseEntity.status(500).body("Error processing notification");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebhookNotification {
        private String paymentId;
        private String status;
        private String statusDetail;
        private String notificationToken;
        private BigDecimal amount;
        private String currency;
        private String transactionId;
        private String payerEmail;
    }
}

// Derly Ortiz Ubiera