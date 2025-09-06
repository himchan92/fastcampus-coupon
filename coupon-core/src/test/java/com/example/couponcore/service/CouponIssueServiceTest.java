package com.example.couponcore.service;

import com.example.couponcore.TestConfig;
import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.example.couponcore.repository.mysql.CouponJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CouponIssueServiceTest extends TestConfig {

    @Autowired
    CouponIssueService sut;

    @Autowired
    CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    //실행전 정리
    @BeforeEach
    void clean() {
        couponJpaRepository.deleteAllInBatch();
        couponIssueJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("쿠폰발급내역이 존재하면 예외를 반환한다.")
    void saveCouponIssue_1() {
        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(1L)
                .userId(1L)
                .build();

        couponIssueJpaRepository.save(couponIssue);

        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.saveCouponIssue(couponIssue.getCouponId(), couponIssue.getUserId());
        });

        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATED_COUPON_ISSUE);
    }

    @Test
    @DisplayName("쿠폰발급내역이 존재안하면 쿠폰을 발급.")
    void saveCouponIssue_2() {
        long couponId = 1L;
        long userId = 1L;

        CouponIssue result = sut.saveCouponIssue(couponId, userId);

        //발급 ID 존재시 TRUE 반환
        Assertions.assertTrue(couponIssueJpaRepository.findById(result.getId()).isPresent());
    }
}