package com.nulvora.nulvora.userCoupon.repository;

import com.nulvora.nulvora.userCoupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);


    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long CouponId);
}
