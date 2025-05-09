package com.DerlyKhipu.Khipu.service;

import com.DerlyKhipu.Khipu.config.KhipuConfig;
import com.DerlyKhipu.Khipu.dto.PaymentRequest;
import com.DerlyKhipu.Khipu.dto.PaymentResponse;
import com.DerlyKhipu.Khipu.dto.PaymentStatusResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KhipuService {
    private final RestTemplate restTemplate;
    private final KhipuConfig khipuConfig;

    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        paymentRequest.setReturnUrl(khipuConfig.getReturnUrl());
        paymentRequest.setCancelUrl(khipuConfig.getCancelUrl());
        paymentRequest.setNotifyUrl(khipuConfig.getWebhookUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", khipuConfig.getApiKey());

        HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);

        try {
            ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(
                    khipuConfig.getApiUrl() + "/v3/payments",
                    request,
                    PaymentResponse.class
            );

            log.info("Pago creado en Khipu: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("Error al crear pago en Khipu", e);
            throw new RuntimeException("Error al crear pago en Khipu", e);
        }
    }

    public PaymentStatusResponse getPaymentStatus(String paymentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", khipuConfig.getApiKey());

        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<PaymentStatusResponse> response = restTemplate.exchange(
                    khipuConfig.getApiUrl() + "/v3/payments/" + paymentId,
                    HttpMethod.GET,
                    request,
                    PaymentStatusResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Error al obtener estado del pago {}", paymentId, e);
            throw new RuntimeException("Error al obtener estado del pago", e);
        }
    }
}

// Derly Ortiz Ubiera