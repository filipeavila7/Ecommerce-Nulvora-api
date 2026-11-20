package com.nulvora.nulvora.productVariant.dto.response;

import java.math.BigDecimal;

public record ProductVariantCartResponse(
        Long id,
        String productName,
        String variantName,
        BigDecimal price,
        String variantImgPrimary
) {
}
