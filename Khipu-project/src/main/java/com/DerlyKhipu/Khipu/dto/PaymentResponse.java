package com.DerlyKhipu.Khipu.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentId;
    private String paymentUrl;
    private String simplifiedTransferUrl;
    private String transferUrl;
    private String webpayUrl;
    private String appUrl;
    private String readyForTerminal;
}

// Derly Ortiz Ubiera