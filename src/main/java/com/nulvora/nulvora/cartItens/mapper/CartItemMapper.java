package com.nulvora.nulvora.cartItens.mapper;

import com.nulvora.nulvora.cartItens.dto.response.CartItemPostResponse;

import com.nulvora.nulvora.cartItens.entity.CartItem;
import com.nulvora.nulvora.productVariant.mapper.ProductVariantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;


@Mapper(componentModel = "spring", uses = ProductVariantMapper.class)
public interface CartItemMapper {

    @Mapping(target = "subtotal", source = "subtotal")
    CartItemPostResponse toResponse(CartItem cartItem, BigDecimal subtotal);
}
