package com.fastcampus.couponcore.repository.mysql;

import com.fastcampus.couponcore.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

}
