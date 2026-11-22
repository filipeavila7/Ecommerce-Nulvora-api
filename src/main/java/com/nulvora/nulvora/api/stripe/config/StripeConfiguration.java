package com.nulvora.nulvora.api.stripe.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class StripeConfiguration {

    // injetar a chave de api do stripe
    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    // executa o metodo depois que o Spring instancia e configura a classe.
    @PostConstruct
    public void init() {
        // configura a autenticação utilizada pelo SDK oficial.
        Stripe.apiKey = secretKey;
    }
}
