package com.nulvora.nulvora.product.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String categoryName,
        String imageUrl,
        String description,
        boolean isActive,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt,
        List<ProductAttributeResponse> attributes,
        List<ProductKeywordResponse> productKeywords

) {
}
