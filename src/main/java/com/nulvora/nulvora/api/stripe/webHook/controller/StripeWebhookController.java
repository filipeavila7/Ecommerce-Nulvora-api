package com.nulvora.nulvora.api.stripe.webHook.controller;



import com.nulvora.nulvora.api.stripe.config.StripeConfiguration;
import com.nulvora.nulvora.payment.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PaymentService paymentService;
    private final StripeConfiguration stripeConfiguration;

    // receber requisição enviada pelo stripe
    @PostMapping
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload, // recebe o corpo da requisição
            @RequestHeader("Stripe-Signature") String signature // recebe a assinatura enviada pelo stripe
    ) {

        Event event;

        try {
            // valida a assinatura e constrói o objeto Event, usando o chave secreta do webHook
            event = Webhook.constructEvent(
                    payload,
                    signature,
                    stripeConfiguration.getWebhookSecret()
            );
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build(); // se for invalida retorna badRequest
        }

        // log de debug

        paymentService.processWebhookEvent(event);

        return ResponseEntity.ok().build();
    }
}