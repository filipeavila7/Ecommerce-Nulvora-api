
package com.nulvora.nulvora.cupom.dto.request;

import com.nulvora.nulvora.cupom.entity.CouponType;
import com.nulvora.nulvora.cupom.entity.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponUpdateRequest(

        // opcional (a service cria um automaticamente)
        @Size(max = 30, message = "O código deve ter no máximo 30 caracteres")
        String code,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String description,

        CouponType couponType,

        DiscountType discountType,

        @DecimalMin(value = "0.01", message = "O desconto máximo deve ser maior que zero")
        BigDecimal maxDiscount,

        @DecimalMin(value = "0.00", message = "O valor mínimo do pedido não pode ser negativo")
        BigDecimal minOrderValue,

        @Future(message = "A data de início deve ser futura")
        LocalDateTime startAt,

        @Positive(message = "O limite de utilizações deve ser maior que zero")
        Integer maxUsagePerUser,

        @Future(message = "A data de expiração deve ser futura")
        LocalDateTime expireAt

) {
}
