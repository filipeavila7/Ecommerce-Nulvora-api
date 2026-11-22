package com.nulvora.nulvora.payment.repository;

import com.nulvora.nulvora.payment.entity.Payment;
import com.nulvora.nulvora.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // localizar o pagamento a partir da sessão do Checkout.
    Optional<Payment> findByStripeCheckoutSessionId(String sessionId);

    // localizar pelo identificador da operação financeira.
    Optional<Payment> findByStripePaymentIntentId(String paymentIntentId);

    // verificar se já existe um pagamento com determinado status para o pedido.
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}
