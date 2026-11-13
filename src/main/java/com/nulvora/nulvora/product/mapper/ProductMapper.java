package com.nulvora.nulvora.product.mapper;

import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.request.ProductUpdateRequest;
import com.nulvora.nulvora.product.dto.response.*;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productKeywords.entity.ProductKeyword;
import com.nulvora.nulvora.productVariant.mapper.ProductVariantMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",  uses = {ProductVariantMapper.class})
public interface ProductMapper {

    // transformar dto em entidade
    Product toEntity(ProductRequest request);

    // transformar produto em productResponse
    @Mapping(target = "attributes", source = "productAttributes")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "isActive", source = "active")
    ProductResponse toProductResponse(Product product);

    // transformar ProductAttribute em ProductAttributeResponse
    ProductAttributeResponse toProductAttributeResponse(ProductAttribute productAttribute);

    // transformar ProductKeyword em ProductKeywordResponse
    ProductKeywordResponse toProductKeywordResponse(ProductKeyword productKeyword);

    // transformar ProductAttributeOption em  AttributeOptionResponse
    AttributeOptionResponse toOptionResponse(ProductAttributeOption option);

    // atualizar somente campos não nulos de product
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    @Mapping(target = "productAttributes", ignore = true)
    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProductUpdateRequest request, @MappingTarget Product product);

    // transformar Product em ProductDetailsResponse
    @Mapping(
            target = "productVariants",
            source = "productVariants",
            qualifiedByName = "activeVariants"
    )
    @Mapping(target = "categoryName", source = "category.name")
    ProductDetailsResponse toDetailsResponse(Product product);
}
