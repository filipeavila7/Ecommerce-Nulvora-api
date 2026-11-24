package com.nulvora.nulvora.promotions.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nulvora.nulvora.coupon.entity.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionResponse(
        Long id,
        String name,
        String description,
        DiscountType discountType,
        BigDecimal discountValue,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime startAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime expireAt,

        boolean active
) {
}