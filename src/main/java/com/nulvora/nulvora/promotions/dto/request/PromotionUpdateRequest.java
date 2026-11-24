package com.nulvora.nulvora.promotions.dto.request;




import com.nulvora.nulvora.coupon.entity.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionUpdateRequest(


        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String name,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String description,

        DiscountType discountType,

        @DecimalMin(value = "0.01", message = "O desconto deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O desconto deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal discountValue,

        @Future(message = "A data de início deve ser futura")
        LocalDateTime startAt,

        @Future(message = "A data de expiração deve ser futura")
        LocalDateTime expireAt

) {

}

