package com.DerlyKhipu.Khipu.controller;

import com.DerlyKhipu.Khipu.dto.PaymentRequest;
import com.DerlyKhipu.Khipu.dto.PaymentResponse;
import com.DerlyKhipu.Khipu.dto.PaymentStatusResponse;
import com.DerlyKhipu.Khipu.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.DerlyKhipu.Khipu.repository.PaymentRepository;
import com.DerlyKhipu.Khipu.service.KhipuService;
import com.DerlyKhipu.Khipu.service.PaymentService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebController {
    private final KhipuService khipuService;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("payments", paymentRepository.findAll());
        return "index";
    }

    @GetMapping("/create-payment")
    public String showCreatePaymentForm(Model model) {
        model.addAttribute("paymentRequest", new PaymentRequest());
        return "create-payment";
    }

    @PostMapping("/create-payment")
    public String createPayment(@ModelAttribute PaymentRequest paymentRequest) {
        PaymentResponse response = khipuService.createPayment(paymentRequest);
        paymentService.createPaymentRecord(paymentRequest, response.getPaymentId());
        return "redirect:/payment/" + response.getPaymentId();
    }

    @GetMapping("/payment/{paymentId}")
    public String viewPayment(@PathVariable String paymentId, Model model) {
        PaymentStatusResponse status = khipuService.getPaymentStatus(paymentId);
        model.addAttribute("payment", status);

        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        model.addAttribute("dbPayment", payment);

        return "payment-status";
    }
}

// Derly Ortiz Ubiera