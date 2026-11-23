package com.nulvora.nulvora.wishList.mapper;

import com.nulvora.nulvora.product.mapper.ProductMapper;
import com.nulvora.nulvora.wishListItem.dto.response.WishListItemResponse;
import com.nulvora.nulvora.wishListItem.entity.WishListItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface WishListMapper {

    WishListItemResponse toWishListItemResponse(WishListItem wishListItem);
}
