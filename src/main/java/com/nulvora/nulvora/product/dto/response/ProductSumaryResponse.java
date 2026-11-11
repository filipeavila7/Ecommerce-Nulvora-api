package com.nulvora.nulvora.product.dto.response;

import java.math.BigDecimal;

public record ProductSumaryResponse(
        Long id,
        String name,
        String imageUrl,
        BigDecimal minorPrice
) {
}
