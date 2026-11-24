package com.nulvora.nulvora.promotionProduct.mapper;

import com.nulvora.nulvora.product.mapper.ProductMapper;
import com.nulvora.nulvora.promotionProduct.dto.response.PromotionProductResponse;
import com.nulvora.nulvora.promotionProduct.entity.PromotionProduct;
import com.nulvora.nulvora.promotions.mapper.PromotionMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, PromotionMapper.class})
public interface PromotionProductMapper {

    PromotionProductResponse toPromotionProductResponse(PromotionProduct promotionProduct);
}
