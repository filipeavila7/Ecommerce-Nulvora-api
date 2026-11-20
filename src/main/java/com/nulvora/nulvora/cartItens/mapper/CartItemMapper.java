package com.nulvora.nulvora.cartItens.mapper;

import com.nulvora.nulvora.cartItens.dto.response.CartItemResponse;
import com.nulvora.nulvora.cartItens.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "subtotal", source = "subtotal")
    CartItemResponse toResponse(CartItem cartItem, BigDecimal subtotal);
}
