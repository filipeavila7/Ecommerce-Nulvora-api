package com.nulvora.nulvora.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ProductDetailsResponse(
        Long id,
        String name,
        String description,
        String categoryName,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt,
        List<ProductAttributeResponse> productAttributes,
        List<ProductVariantResponse> productVariants


) {
}
