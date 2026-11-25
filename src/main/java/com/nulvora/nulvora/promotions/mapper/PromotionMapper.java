package com.nulvora.nulvora.promotions.mapper;

import com.nulvora.nulvora.promotionProduct.mapper.PromotionProductMapper;
import com.nulvora.nulvora.promotions.dto.request.PromotionRequest;
import com.nulvora.nulvora.promotions.dto.request.PromotionUpdateRequest;
import com.nulvora.nulvora.promotions.dto.response.PromotionDetailsResponse;
import com.nulvora.nulvora.promotions.dto.response.PromotionResponse;
import com.nulvora.nulvora.promotions.entity.Promotion;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = PromotionProductMapper.class)
public interface PromotionMapper {

    Promotion toEntity(PromotionRequest request);

    PromotionResponse toPromotionResponse(Promotion promotion);

    PromotionDetailsResponse toPromotionDetailsResponse(Promotion promotion);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PromotionUpdateRequest request, @MappingTarget Promotion promotion);
}
