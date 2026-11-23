package com.nulvora.nulvora.wishListItem.dto.response;


import com.nulvora.nulvora.product.dto.response.ProductSumaryResponse;

import java.time.LocalDateTime;

public record WishListItemResponse(
        LocalDateTime createdAt,
        Long id,
        ProductSumaryResponse product
) {
}
