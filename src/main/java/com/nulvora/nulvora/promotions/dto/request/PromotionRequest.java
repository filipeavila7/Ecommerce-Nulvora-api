package com.nulvora.nulvora.promotions.dto.request;




import com.nulvora.nulvora.coupon.entity.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionRequest(

        @NotBlank(message = "O nome da promoção é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String name,

        @NotBlank(message = "A descrição da promoção é obrigatória")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String description,

        @NotNull(message = "O tipo de desconto é obrigatório")
        DiscountType discountType,

        @NotNull(message = "O valor do desconto é obrigatório")
        @DecimalMin(value = "0.01", message = "O desconto deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O desconto deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal discountValue,

        @NotNull(message = "A data de início é obrigatória")
        @Future(message = "A data de início deve ser futura")
        LocalDateTime startAt,

        @NotNull(message = "A data de expiração é obrigatória")
        @Future(message = "A data de expiração deve ser futura")
        LocalDateTime expireAt

) {
    @AssertTrue(message = "A data de expiração deve ser posterior à data de início")
    public boolean isExpireAtAfterStartAt() {
        if (startAt == null || expireAt == null) {
            return true;
        }

        return expireAt.isAfter(startAt);
    }
}

