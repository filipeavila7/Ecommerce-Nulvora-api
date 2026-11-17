package com.nulvora.nulvora.cupom.mapper;

import com.nulvora.nulvora.cupom.dto.request.CouponRequest;
import com.nulvora.nulvora.cupom.dto.request.CouponUpdateRequest;
import com.nulvora.nulvora.cupom.dto.response.CouponResponse;
import com.nulvora.nulvora.cupom.entity.Coupon;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    // transforma CouponRequest em Coupon
    Coupon toEntity(CouponRequest request);

    // transformar Coupon em CouponRequest
    CouponResponse toResponse(Coupon coupon);

    // atualiza somente os campos que não forem nulos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CouponUpdateRequest request, @MappingTarget Coupon coupon);
}
