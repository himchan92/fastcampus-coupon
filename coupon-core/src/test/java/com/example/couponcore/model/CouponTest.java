package com.example.couponcore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CouponTest {

    @Test
    @DisplayName("발급수량이 남아있다면 true")
    void availableIssueQuantity_1() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .build();

        boolean result = coupon.availableIssueQuantity();

        assertTrue(result);
    }

    @Test
    @DisplayName("발급수량이 소진되었으면 false")
    void availableIssueQuantity_2() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .build();

        boolean result = coupon.availableIssueQuantity();

        assertFalse(result);
    }

    @Test
    @DisplayName("발급기한이 시작안되었으면 false")
    void availableIssueQuantity_3() {
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().plusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        boolean result = coupon.availableIssueDate();

        assertFalse(result);
    }
}