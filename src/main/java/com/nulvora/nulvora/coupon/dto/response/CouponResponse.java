package com.nulvora.nulvora.coupon.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nulvora.nulvora.coupon.entity.CouponType;
import com.nulvora.nulvora.coupon.entity.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse(
        Long id,
        String code,
        String description,
        CouponType couponType,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal minOrderValue,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime startAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime expireAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt,

        Integer maxUsagePerUser,
        boolean activate

) {
}
