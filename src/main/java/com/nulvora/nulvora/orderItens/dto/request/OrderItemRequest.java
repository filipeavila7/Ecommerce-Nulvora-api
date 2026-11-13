package com.nulvora.nulvora.orderItens.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(

        @NotNull
        Long productVariantId,

        @NotNull
        @Min(1)
        Integer quantity

) {}