package com.DerlyKhipu.Khipu.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentStatusResponse {
    private String paymentId;
    private String status;
    private String statusDetail;
    private BigDecimal amount;
    private String currency;
    private String transactionId;
    private String payerEmail;
    private String notificationToken;
    private String paymentUrl;
}

// Derly Ortiz Ubiera