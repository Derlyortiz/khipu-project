package com.DerlyKhipu.Khipu.dto;

import lombok.Data;

@Data
public class WebhookNotification {
    private String paymentId;
    private String status;
    private String statusDetail;
    private String notificationToken;
}

// Derly Ortiz Ubiera