package com.nulvora.nulvora.productImg.mapper;

import com.nulvora.nulvora.productImg.dto.request.ProductImgUpdateRequest;
import com.nulvora.nulvora.productImg.entity.ProductImg;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductImgMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    @Mapping(target = "primary", source = "isPrimary")
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            ProductImgUpdateRequest request,
            @MappingTarget ProductImg productImg
    );
}
