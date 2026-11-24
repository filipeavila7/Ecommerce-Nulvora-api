package com.nulvora.nulvora.promotions.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nulvora.nulvora.coupon.entity.DiscountType;
import com.nulvora.nulvora.promotionProduct.dto.response.PromotionProductDetailsResponse;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PromotionDetailsResponse(
        Long id,
        String name,
        String description,
        DiscountType discountType,
        BigDecimal discountValue,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime startAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime expireAt,

        boolean active,
        List<PromotionProductDetailsResponse> promotionProducts // lista de produtos relacionados à promoção


) {
}
