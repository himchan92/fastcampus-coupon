package com.example.couponcore.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponTest {

  @Test
  @DisplayName("발급 수량이 남아있다면 true 반환")
  void availableIssueQuantity_1() {
    //given
    Coupon coupon = Coupon.builder()
        .totalQuantity(100)
        .issuedQuantity(99)
        .build();

    //when
    boolean result = coupon.availableIssueQuantity();

    //then
    Assertions.assertTrue(result);
  }
}