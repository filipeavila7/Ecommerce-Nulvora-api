package com.nulvora.nulvora.order.dto.response;

import com.nulvora.nulvora.coupon.dto.response.CouponResponse;
import com.nulvora.nulvora.order.entity.OrderStatus;
import com.nulvora.nulvora.orderItens.dto.response.OrderItemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        BigDecimal discount,
        BigDecimal total,
        BigDecimal shippingCost, // preço do frete
        BigDecimal subTotal,
        LocalDateTime createdAt,
        List<OrderItemResponse> orderItems,
        CouponResponse coupon


) {
}
