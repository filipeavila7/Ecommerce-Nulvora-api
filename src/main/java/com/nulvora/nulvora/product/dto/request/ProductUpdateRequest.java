package com.nulvora.nulvora.product.dto.request;

import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(

        @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracters")
        String name,

        @Size(max = 500, message = "A descrição do produto deve ter no máximo 500 caracters")
        String description,

        @Size(max = 300, message = "A url da imagem é muito grande")
        String imageUrl,

        Boolean active,

        Long categoryId
) {
}
