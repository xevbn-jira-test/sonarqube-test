package com.example.sonarqubetest.payment;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@Transactional
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse pay(@Valid @RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }

    @GetMapping("/{orderId}")
    public PaymentResponse findByOrderId(@PathVariable String orderId) {
        return paymentService.findByOrderId(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public PaymentResponse cancel(@PathVariable String orderId) {
        return paymentService.cancel(orderId);
    }
}
