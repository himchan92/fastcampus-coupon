package com.fastcampus.couponcore.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("발급수량이 소진되었다면 false")
    void availableIssueQuantity_2() {
        Coupon coupon = Coupon.builder()
            .totalQuantity(100)
            .issuedQuantity(100)
            .build();

        boolean result = coupon.availableIssueQuantity();

        assertFalse(result);
    }

    @Test
    @DisplayName("최대 발급 수량이 설정안되어쓰면 true")
    void availableIssueQuantity_3() {
        Coupon coupon = Coupon.builder()
            .totalQuantity(null)
            .issuedQuantity(100)
            .build();

        boolean result = coupon.availableIssueQuantity();

        assertTrue(result);
    }

    @Test
    @DisplayName("발급기간 시작안되었다면 false")
    void availableIssueDate_1() {
        Coupon coupon = Coupon.builder()
            .dateIssueStart(LocalDateTime.now().plusDays(1))
            .dateIssueEnd(LocalDateTime.now().plusDays(2))
            .build();

        boolean result = coupon.availableIssueDate();

        assertFalse(result);
    }

    @Test
    @DisplayName("발급기간 해당되면 true")
    void availableIssueDate_2() {
        Coupon coupon = Coupon.builder()
            .dateIssueStart(LocalDateTime.now().minusDays(1))
            .dateIssueEnd(LocalDateTime.now().plusDays(2))
            .build();

        boolean result = coupon.availableIssueDate();

        assertTrue(result);
    }

    @Test
    @DisplayName("발급기간 종료되면 false")
    void availableIssueDate_3() {
        Coupon coupon = Coupon.builder()
            .dateIssueStart(LocalDateTime.now().minusDays(2))
            .dateIssueEnd(LocalDateTime.now().minusDays(1))
            .build();

        boolean result = coupon.availableIssueDate();

        assertFalse(result);
    }

    @Test
    @DisplayName("발급 수량과 발급 기간이 유효하다면 발급성공")
    void issue_1() {
        Coupon coupon = Coupon.builder()
            .totalQuantity(100)
            .issuedQuantity(99)
            .dateIssueStart(LocalDateTime.now().minusDays(1))
            .dateIssueEnd(LocalDateTime.now().plusDays(2))
            .build();

        coupon.issue();

        assertEquals(coupon.getIssuedQuantity(), 100);
    }
}