package com.example.couponcore.model;

import com.example.couponcore.exception.CouponIssueException;
import java.time.LocalDateTime;
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

  @Test
  @DisplayName("발급 수량이 설정안되었으면 true 반환")
  void availableIssueQuantity_2() {
    //given
    Coupon coupon = Coupon.builder()
        .totalQuantity(null)
        .issuedQuantity(100)
        .build();

    //when
    boolean result = coupon.availableIssueQuantity();

    //then
    Assertions.assertTrue(result);
  }

  @Test
  @DisplayName("발급 수량을 초과 시 예외 발생")
  void issue_2() {
    //given
    Coupon coupon = Coupon.builder()
        .totalQuantity(100)
        .issuedQuantity(100)
        .dateIssueStart(LocalDateTime.now().minusDays(1))
        .dateIssueEnd(LocalDateTime.now().plusDays(2))
        .build();

    //when & then
    CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, coupon::issue);
  }
}