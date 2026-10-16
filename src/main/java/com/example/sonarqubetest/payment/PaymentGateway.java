package com.example.sonarqubetest.payment;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 외부 PG사 연동을 흉내 내는 가짜 게이트웨이.
 */
@Component
public class PaymentGateway {

    public String approve(String orderId, double amount) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // ignore
        }
        return "PG-" + UUID.randomUUID();
    }

    public void cancel(String pgTransactionId) {
        // 외부 PG 취소 API 호출
    }
}
