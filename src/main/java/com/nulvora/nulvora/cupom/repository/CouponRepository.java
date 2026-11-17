package com.nulvora.nulvora.cupom.repository;

import com.nulvora.nulvora.cupom.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
