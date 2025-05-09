package com.DerlyKhipu.Khipu.controller;

import com.DerlyKhipu.Khipu.dto.PaymentRequest;
import com.DerlyKhipu.Khipu.dto.PaymentResponse;
import com.DerlyKhipu.Khipu.dto.PaymentStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.validation.Valid;
import com.DerlyKhipu.Khipu.service.KhipuService;
import com.DerlyKhipu.Khipu.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final KhipuService khipuService;
    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = khipuService.createPayment(paymentRequest);
        paymentService.createPaymentRecord(paymentRequest, response.getPaymentId());
        return response;
    }

    @GetMapping("/{paymentId}/status")
    public PaymentStatusResponse getPaymentStatus(@PathVariable String paymentId) {
        return khipuService.getPaymentStatus(paymentId);
    }

    @GetMapping("/{paymentId}/redirect")
    public RedirectView redirectToPayment(@PathVariable String paymentId) {
        PaymentStatusResponse status = khipuService.getPaymentStatus(paymentId);
        return new RedirectView(status.getPaymentUrl());
    }
}

// Derly Ortiz Ubiera