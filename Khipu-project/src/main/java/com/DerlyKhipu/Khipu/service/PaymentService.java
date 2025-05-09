package com.DerlyKhipu.Khipu.service;

import com.DerlyKhipu.Khipu.controller.KhipuWebhookController;
import com.DerlyKhipu.Khipu.dto.PaymentRequest;
import com.DerlyKhipu.Khipu.dto.PaymentStatusResponse;
import com.DerlyKhipu.Khipu.entity.Payment;
import com.DerlyKhipu.Khipu.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final KhipuService khipuService; // Añadido
    private final EmailService emailService; // Añadido

    @Transactional
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void processPaymentNotification(KhipuWebhookController.WebhookNotification notification) {
        log.info("Procesando notificación para pago {}", notification.getPaymentId());

        paymentRepository.updateStatus(
                notification.getPaymentId(),
                notification.getStatus(),
                notification.getStatusDetail()
        );

        log.info("Pago {} actualizado a estado {}", notification.getPaymentId(), notification.getStatus());
    }

    @Transactional
    public Payment createPaymentRecord(PaymentRequest request, String paymentId) {
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .transactionId(request.getTransactionId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status("PENDING")
                .statusDetail("Pendiente de pago")
                .build();

        return paymentRepository.save(payment);
    }

    public void completePayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (payment.getPayerEmail() != null) {
            emailService.sendPaymentNotification(
                    payment.getPayerEmail(),
                    payment.getPaymentId(),
                    payment.getStatus()
            );
        }

        log.info("Producto/servicio liberado para el pago {}", paymentId);
    }

    @Scheduled(fixedRate = 3600000) // Cada hora
    public void checkPendingPayments() {
        log.info("Verificando pagos pendientes...");
        List<Payment> pendingPayments = paymentRepository
                .findByStatusAndCreatedAtBefore(
                        "PENDING",
                        LocalDateTime.now().minusHours(1)
                );

        pendingPayments.forEach(payment -> {
            try {
                PaymentStatusResponse status = khipuService.getPaymentStatus(payment.getPaymentId());
                if (!"PENDING".equals(status.getStatus())) {
                    processPaymentNotification(convertToWebhookNotification(status));
                }
            } catch (Exception e) {
                log.error("Error verificando pago {}", payment.getPaymentId(), e);
            }
        });
    }

    private KhipuWebhookController.WebhookNotification convertToWebhookNotification(PaymentStatusResponse status) {
        return new KhipuWebhookController.WebhookNotification(
                status.getPaymentId(),
                status.getStatus(),
                status.getStatusDetail(),
                status.getNotificationToken(),
                status.getAmount(),
                status.getCurrency(),
                status.getTransactionId(),
                status.getPayerEmail()
        );
    }
}

// Derly Ortiz Ubiera