package com.nulvora.nulvora.productVariant.mapper;

import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productImg.dto.response.ProductImgResponse;
import com.nulvora.nulvora.productImg.entity.ProductImg;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantRequest;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantUpdateRequest;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantAttributesResponse;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.variantAttribute.entity.VariantAttribute;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "productImgs", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @Mapping(target = "defaultVariant", constant = "true")
    @Mapping(target = "active", constant = "true")
    ProductVariant toDefaultVariant(Product product);


    ProductVariant toEntity(ProductVariantRequest request);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "productImgs", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            ProductVariantUpdateRequest request,
            @MappingTarget ProductVariant productVariant
    );

    @Named("activeVariants")
    default List<ProductVariantResponse> mapActiveVariants(
            List<ProductVariant> variants
    ) {
        return variants.stream()
                .filter(ProductVariant::isActive)
                .map(this::toProductVariantResponse)
                .toList();
    }


    // o campo attributes do dto se refere a lista de variantAttributes
    @Mapping(target = "attributes", source = "variantAttributes")
    @Mapping(target = "productImgs", source = "productImgs")
    @Mapping(target = "isDefault", source = "defaultVariant")
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    // o mapper tenta encontrar algum metodo que recebe "variantAttributes" e acha esse
    @Mapping(
            target = "attributeName", // o campo attributeName vai receber o nome do atributo (navega entre as chaves)
            source = "option.productAttribute.name"
    )
    @Mapping(
            target = "optionName", // campo optionName recebe o valor
            source = "option.value"
    )
    ProductVariantAttributesResponse toAttributeResponse(
            VariantAttribute variantAttribute
    );

    @Mapping(target = "primary", source = "primary")
    ProductImgResponse toImgResponse(ProductImg productImg);
}
