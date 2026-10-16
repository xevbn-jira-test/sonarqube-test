package com.example.sonarqubetest.payment;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String orderId,
        double amount,
        PaymentStatus status,
        String pgTransactionId,
        LocalDateTime createdAt
) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPgTransactionId(),
                payment.getCreatedAt()
        );
    }
}
