package com.nulvora.nulvora.userCoupon.dto.response;

import com.nulvora.nulvora.cupom.dto.response.CouponResponse;
import com.nulvora.nulvora.user.dto.response.UserResponse;

import java.time.LocalDateTime;

public record UserCouponResponse(
        Long id,
        Integer usageLimit,
        Integer usageCount,
        LocalDateTime assignedAt,
        UserResponse user,
        CouponResponse coupon
) {
}
