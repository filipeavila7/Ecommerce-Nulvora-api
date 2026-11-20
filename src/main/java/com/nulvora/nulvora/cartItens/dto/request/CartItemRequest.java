package com.nulvora.nulvora.cartItens.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;



public record CartItemRequest(

        @NotNull
        @Positive
        Long productVariantId,

        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Integer quantity
) {
}
