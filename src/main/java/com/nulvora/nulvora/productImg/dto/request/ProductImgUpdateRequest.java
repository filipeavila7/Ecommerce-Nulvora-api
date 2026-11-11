package com.nulvora.nulvora.productImg.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProductImgUpdateRequest(
        @NotNull
        Long id,

        String imageUrl,

        Boolean isPrimary
) {
}
