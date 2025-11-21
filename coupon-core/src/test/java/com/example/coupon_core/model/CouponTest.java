package com.example.coupon_core.model;

import com.example.coupon_core.exception.CouponIssueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("발급 수량이 남아있다면 true")
    void availableIssueQuantity1() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .build();

        boolean result = coupon.availableIssueQuantity();

        assertTrue(result);
    }

    @Test
    @DisplayName("발급 수량이 소진되었으면 false")
    void availableIssueQuantity2() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .build();

        boolean result = coupon.availableIssueQuantity();

        assertFalse(result);
    }

    @Test
    @DisplayName("발급 수량이 설정되지 않았다면 true")
    void availableIssueQuantity3() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(null)
                .issuedQuantity(100)
                .build();

        boolean result = coupon.availableIssueQuantity();

        assertTrue(result);
    }

    @Test
    @DisplayName("발급 기한 아니면 예외 발생")
    void issue() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .dateIssueStart(LocalDateTime.now().plusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        assertThrows(CouponIssueException.class, coupon::issue);
    }
}