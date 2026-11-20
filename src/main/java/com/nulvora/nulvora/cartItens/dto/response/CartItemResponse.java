package com.nulvora.nulvora.cartItens.dto.response;

import com.nulvora.nulvora.productVariant.dto.response.ProductVariantCartResponse;


import java.math.BigDecimal;
import java.util.List;

public record CartItemResponse(
        Long id,
        List<ProductVariantCartResponse> productVariant,
        Integer quantity,
        BigDecimal subtotal

) {
}
