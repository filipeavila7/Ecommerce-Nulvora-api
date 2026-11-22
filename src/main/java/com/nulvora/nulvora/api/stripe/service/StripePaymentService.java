package com.nulvora.nulvora.api.stripe.service;

import com.nulvora.nulvora.api.stripe.dto.response.StripeCheckoutResponse;
import com.nulvora.nulvora.payment.entity.Payment;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripePaymentService {

    public StripeCheckoutResponse createCheckoutSession(Payment payment)
            throws StripeException {

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment/success")
                .setCancelUrl("http://localhost:5173/payment/cancel")

                // Identifica nosso pagamento dentro do Stripe
                .setClientReferenceId(payment.getId().toString())
                .putMetadata("paymentId", payment.getId().toString())
                .putMetadata("orderId", payment.getOrder().getId().toString())

                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("brl")
                                                .setUnitAmount(
                                                        payment.getAmount()
                                                                .multiply(BigDecimal.valueOf(100))
                                                                .longValueExact()
                                                )
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Pedido #" + payment.getOrder().getId())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        // O SDK envia uma requisição para a API do Stripe.
        Session session = Session.create(params);

        // e devolve isso
        return new StripeCheckoutResponse(
                session.getId(),
                session.getUrl()
        );
    }
}