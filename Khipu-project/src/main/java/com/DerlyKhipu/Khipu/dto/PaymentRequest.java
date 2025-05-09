package com.DerlyKhipu.Khipu.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotBlank(message = "El concepto es requerido")
    private String subject;
    
    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal amount;
    
    @NotBlank(message = "La moneda es requerida")
    private String currency = "CLP";
    
    @NotBlank(message = "El ID de transacción es requerido")
    private String transactionId;
    
    private String returnUrl;
    private String cancelUrl;
    private String notifyUrl;
    private String pictureUrl;
    private String custom;
}

// Derly Ortiz Ubiera