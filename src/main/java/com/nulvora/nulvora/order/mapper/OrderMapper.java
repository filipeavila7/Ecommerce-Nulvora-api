package com.nulvora.nulvora.order.mapper;

import com.nulvora.nulvora.coupon.entity.Coupon;
import com.nulvora.nulvora.coupon.mapper.CouponMapper;
import com.nulvora.nulvora.order.dto.response.OrderResponse;
import com.nulvora.nulvora.order.entity.Order;
import com.nulvora.nulvora.orderItens.dto.response.OrderItemResponse;
import com.nulvora.nulvora.orderItens.entity.OrderItem;
import com.nulvora.nulvora.productImg.entity.ProductImg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CouponMapper.class)
public interface OrderMapper {


    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productName", source = "productVariant.product.name")
    @Mapping(target = "variantName", source = "productVariant.name")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "imageUrl", source = "productVariant.productImgs")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    default String getPrimaryImageUrl(List<ProductImg> images) {
        return images.stream()
                .filter(ProductImg::isPrimary)
                .map(ProductImg::getImageUrl)
                .findFirst()
                .orElse(null);
    }
}
