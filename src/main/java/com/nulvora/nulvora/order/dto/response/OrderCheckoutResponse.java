package com.nulvora.nulvora.order.dto.response;

public record OrderCheckoutResponse(
        OrderResponse order,
        String checkoutUrl
) {
}
