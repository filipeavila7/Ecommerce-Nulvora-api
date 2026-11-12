package com.nulvora.nulvora.product.dto.response;

import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ProductDetailsResponse(
        Long id,
        String name,
        String description,
        String categoryName,
        LocalDateTime createdAt,
        List<ProductAttributeResponse> productAttributes,
        List<ProductVariantResponse> productVariants


) {
}
