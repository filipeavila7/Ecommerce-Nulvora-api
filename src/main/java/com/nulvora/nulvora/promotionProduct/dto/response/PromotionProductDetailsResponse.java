package com.nulvora.nulvora.promotionProduct.dto.response;

import com.nulvora.nulvora.product.dto.response.ProductResponse;
import com.nulvora.nulvora.promotions.dto.response.PromotionResponse;

public record PromotionProductDetailsResponse(
        Long id,
        ProductResponse product

) {
}
