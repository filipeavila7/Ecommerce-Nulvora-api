package com.nulvora.nulvora.productVariant.dto.request;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record ProductVariantUpdateRequest(
        @Size(max = 30, message = "O sku deve ter no mínimo 30 caracteres")
        String sku,

        @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracters")
        String name,

        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        @Digits(integer = 10, fraction = 2, message = "O preço deve ter no máximo 10 dígitos inteiros e 2 decimais")
        BigDecimal price,

        @PositiveOrZero(message = "O estoque não pode ser negativo")
        Integer stockQuantity,

        Set<@NotNull Long> optionsIds

) {
}
