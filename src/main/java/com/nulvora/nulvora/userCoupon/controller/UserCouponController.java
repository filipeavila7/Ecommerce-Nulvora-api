package com.nulvora.nulvora.userCoupon.controller;

import com.nulvora.nulvora.userCoupon.dto.response.UserCouponResponse;
import com.nulvora.nulvora.userCoupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-coupon")
public class UserCouponController {
    private final UserCouponService userCouponService;

    // resgatr cupom
    @PostMapping("/{couponId}/assign")
    public ResponseEntity<UserCouponResponse> createUserCoupon(
            @PathVariable Long couponId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userCouponService.createUserCoupon(couponId));
    }
}
