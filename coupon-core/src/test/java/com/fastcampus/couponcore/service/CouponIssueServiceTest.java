package com.fastcampus.couponcore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fastcampus.couponcore.CouponCoreConfiguration;
import com.fastcampus.couponcore.exception.CouponIssueException;
import com.fastcampus.couponcore.exception.ErrorCode;
import com.fastcampus.couponcore.model.CouponIssue;
import com.fastcampus.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.fastcampus.couponcore.repository.mysql.CouponIssueRepository;
import com.fastcampus.couponcore.repository.mysql.CouponJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CouponCoreConfiguration.class)
class CouponIssueServiceTest {

    @Autowired
    CouponIssueService sut;

    @Autowired
    CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    CouponIssueRepository couponIssueRepository;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @BeforeEach //테스트 실행전 클리어
    void clean() {
        couponJpaRepository.deleteAllInBatch(); //JpaRepository 제공 메소드
        couponIssueJpaRepository.deleteAllInBatch(); //JpaRepository 제공 메소드
    }

    @Test
    @DisplayName("쿠폰내역발급이 존재한다면 예외")
    void test() {
        CouponIssue couponIssue = CouponIssue.builder()
            .couponId(1L)
            .userId(1L)
            .build();

        couponIssueJpaRepository.save(couponIssue);

        //TODO : 예외 잘던져지는지 수행
        CouponIssueException exception = assertThrows(CouponIssueException.class, ()->{
            sut.saveCouponIssue(couponIssue.getCouponId(), couponIssue.getUserId());
        });

        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATE_COUPON_ISSUE);
    }
}