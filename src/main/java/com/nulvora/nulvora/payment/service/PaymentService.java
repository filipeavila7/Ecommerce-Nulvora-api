package com.nulvora.nulvora.payment.service;

import com.nulvora.nulvora.api.stripe.dto.response.StripeCheckoutResponse;
import com.nulvora.nulvora.api.stripe.service.StripePaymentService;
import com.nulvora.nulvora.order.entity.Order;
import com.nulvora.nulvora.payment.entity.Payment;
import com.nulvora.nulvora.payment.entity.PaymentStatus;
import com.nulvora.nulvora.payment.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


// responsável por registrar o pagamento no banco.

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final StripePaymentService stripePaymentService;


    // criar pagamento no banco
    public Payment createPayment(Order order) {

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(order.getTotal());
        payment.setStatus(PaymentStatus.PENDING);

        return paymentRepository.save(payment);
    }

    // incicializar o pagamento do stripe
    public StripeCheckoutResponse initializePayment(Payment payment)
            throws StripeException {

        // solicita ao Stripe a criação de uma sessão de Checkout
        StripeCheckoutResponse response =
                stripePaymentService.createCheckoutSession(payment);

        // adciona na entidade de pagamento
        payment.setStripeCheckoutSessionId(response.sessionId());

        paymentRepository.save(payment);

        return response;
    }

}
