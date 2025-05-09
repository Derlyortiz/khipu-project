package com.DerlyKhipu.Khipu.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    
    @Async
    public void sendPaymentNotification(String to, String paymentId, String status) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Estado de tu pago #" + paymentId);
            message.setText(String.format(
                "El estado de tu pago %s ha cambiado a: %s\n\n" +
                "Gracias por tu compra!",
                paymentId, status
            ));
            
            mailSender.send(message);
            log.info("Email de notificación enviado a {}", to);
        } catch (Exception e) {
            log.error("Error enviando email de notificación", e);
        }
    }
}

// Derly Ortiz Ubiera