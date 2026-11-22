package com.nulvora.nulvora.api.stripe.dto.response;

public record StripeCheckoutResponse(
        String sessionId,
        String checkoutUrl
) {
}
