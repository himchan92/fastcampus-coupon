package com.example.coupon_core.service;

import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.exception.ErrorCode;
import com.example.coupon_core.model.Coupon;
import com.example.coupon_core.model.CouponIssue;
import com.example.coupon_core.repository.mysql.CouponIssueJpaRepository;
import com.example.coupon_core.repository.mysql.CouponIssueRepository;
import com.example.coupon_core.repository.mysql.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.coupon_core.exception.ErrorCode.COUPON_NOT_EXIST;
import static com.example.coupon_core.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issue(long couponId, long userId) {
        Coupon coupon = findCoupon(couponId); // ID기준 쿠폰존재하는지 체크
        coupon.issue(); //쿠폰존재하면 수량 및 기간 체크
        saveCouponIssue(couponId, userId); //이미 발급된 쿠폰인지 체크 후 없으면 저장
    }

    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId) {
        return couponJpaRepository.findById(couponId)
                .orElseThrow(() -> {
                    throw new CouponIssueException(COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. %s".formatted(couponId));
                });
    }

    @Transactional
    public CouponIssue saveCouponIssue(long couponId, long userId) {
        checkAlreadyIssuance(couponId, userId);
        CouponIssue issue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();

        return couponIssueJpaRepository.save(issue);
    }

    private void checkAlreadyIssuance(long couponId, long userId) {
        CouponIssue issue = couponIssueRepository.findFirstCouponIssue(couponId, userId);
        if(issue != null) {
            //이미 발급된 경우 예외처리
            throw new CouponIssueException(DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰입니다. user_id: %s, couponId: %s".formatted(userId, couponId));
        }
    }
}