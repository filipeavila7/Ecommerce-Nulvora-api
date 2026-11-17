package com.nulvora.nulvora.cupom.dto.response;

import com.nulvora.nulvora.cupom.entity.CouponType;
import com.nulvora.nulvora.cupom.entity.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse(
        Long id,
        String code,
        String description,
        CouponType couponType,
        DiscountType discountType,
        BigDecimal maxDiscount,
        BigDecimal minOrderValue,
        LocalDateTime startAt,
        LocalDateTime expireAt,
        LocalDateTime createAt,
        boolean activate

) {
}
