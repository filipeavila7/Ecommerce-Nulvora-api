package com.nulvora.nulvora.product.dto.response;


import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String categoryName,
        String imageUrl,
        String description,
        boolean isActive,
        LocalDateTime createdAt,
        List<ProductAttributeResponse> attributes,
        List<ProductKeywordResponse> productKeywords

) {
}
