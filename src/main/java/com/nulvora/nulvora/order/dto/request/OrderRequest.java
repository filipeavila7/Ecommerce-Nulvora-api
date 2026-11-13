package com.nulvora.nulvora.order.dto.request;

import com.nulvora.nulvora.orderItens.dto.request.OrderItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(

        @NotEmpty
        List<@Valid OrderItemRequest> items, // lista com id e quantidade dos produtos

        Long userCouponId // opcional

) {}