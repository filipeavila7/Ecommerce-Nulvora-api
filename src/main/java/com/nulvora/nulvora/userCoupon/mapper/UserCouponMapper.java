package com.nulvora.nulvora.userCoupon.mapper;

import com.nulvora.nulvora.cupom.mapper.CouponMapper;
import com.nulvora.nulvora.user.mapper.UserMapper;
import com.nulvora.nulvora.userCoupon.dto.response.UserCouponResponse;
import com.nulvora.nulvora.userCoupon.entity.UserCoupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                UserMapper.class,
                CouponMapper.class
        })
public interface UserCouponMapper {

    UserCouponResponse toResponse(UserCoupon userCoupon);
}
