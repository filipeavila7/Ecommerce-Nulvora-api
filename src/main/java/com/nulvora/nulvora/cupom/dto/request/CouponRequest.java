
package com.nulvora.nulvora.cupom.dto.request;

import com.nulvora.nulvora.cupom.entity.CouponType;
import com.nulvora.nulvora.cupom.entity.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRequest(

        // opcional (a service cria um automaticamente)
        @Size(max = 30, message = "O código deve ter no máximo 30 caracteres")
        String code,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String description,

        @NotNull(message = "O tipo do cupom é obrigatório")
        CouponType couponType,

        @NotNull(message = "O tipo de desconto é obrigatório")
        DiscountType discountType,

        @NotNull(message = "O desconto máximo é obrigatório")
        @DecimalMin(value = "0.01", message = "O desconto máximo deve ser maior que zero")
        BigDecimal maxDiscount,

        @NotNull(message = "O valor mínimo do pedido é obrigatório")
        @DecimalMin(value = "0.00", message = "O valor mínimo do pedido não pode ser negativo")
        BigDecimal minOrderValue,

        @NotNull(message = "A data de início é obrigatória")
        @Future(message = "A data de início deve ser futura")
        LocalDateTime startAt,

        @NotNull
        @Positive(message = "O limite de utilizações deve ser maior que zero")
        Integer maxUsagePerUser,

        @NotNull(message = "A data de expiração é obrigatória")
        @Future(message = "A data de expiração deve ser futura")
        LocalDateTime expireAt

) {
}
