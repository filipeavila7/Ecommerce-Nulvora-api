package com.nulvora.nulvora.coupon.controller;

import com.nulvora.nulvora.coupon.dto.request.CouponRequest;
import com.nulvora.nulvora.coupon.dto.request.CouponUpdateRequest;
import com.nulvora.nulvora.coupon.dto.response.CouponResponse;
import com.nulvora.nulvora.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;


    // Criar cupom
    @PostMapping("/new")
    public ResponseEntity<CouponResponse> createCoupon(
            @RequestBody @Valid CouponRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(couponService.createCoupon(request));
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable Long couponId,
            @RequestBody @Valid CouponUpdateRequest request
    ) {
        return ResponseEntity.ok(couponService.updateCoupon(couponId, request));
    }
}
