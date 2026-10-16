package com.example.sonarqubetest.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    public PaymentResponse pay(PaymentRequest request) {
        Payment payment = paymentRepository.save(new Payment(request.orderId(), request.amount()));

        try {
            String pgTransactionId = paymentGateway.approve(request.orderId(), request.amount());
            payment.approve(pgTransactionId);
        } catch (Exception e) {
            payment.fail();
        }

        saveHistory(payment);
        return PaymentResponse.from(payment);
    }

    public PaymentResponse findByOrderId(String orderId) {
        return PaymentResponse.from(paymentRepository.findByOrderId(orderId).get());
    }

    public PaymentResponse cancel(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).get();
        paymentGateway.cancel(payment.getPgTransactionId());
        payment.cancel();
        return PaymentResponse.from(payment);
    }

    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    private void saveHistory(Payment payment) {
        paymentRepository.save(payment);
    }
}
