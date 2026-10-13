package com.example.sonarqubetest.payment;

import jakarta.validation.constraints.NotBlank;

public record PaymentRequest(
        @NotBlank String orderId,
        double amount
) {
}
