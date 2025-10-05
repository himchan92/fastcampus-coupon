package com.fastcampus.couponcore.service;

import static com.fastcampus.couponcore.exception.ErrorCode.COUPON_NOT_EXIST;
import static com.fastcampus.couponcore.exception.ErrorCode.DUPLICATE_COUPON_ISSUE;

import com.fastcampus.couponcore.exception.CouponIssueException;
import com.fastcampus.couponcore.exception.ErrorCode;
import com.fastcampus.couponcore.model.Coupon;
import com.fastcampus.couponcore.model.CouponIssue;
import com.fastcampus.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.fastcampus.couponcore.repository.mysql.CouponIssueRepository;
import com.fastcampus.couponcore.repository.mysql.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponIssueRepository couponIssueRepository;

    // TODO : 실제 쿠폰 발급 및 검증 수행
    @Transactional
    public void issue(long couponId, long userId) {
        Coupon coupon = findCoupon(couponId);
        coupon.issue();
        saveCouponIssue(couponId, userId);
    }

    // TODO : 쿠폰 ID 받아와 조회
    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId) {
        return couponJpaRepository.findById(couponId)
            .orElseThrow(() -> {
               throw new CouponIssueException(COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. %s".formatted(couponId));
            });
    }

    // TODO: 이미 발급 받았는지 체크후 어느사용자가 어느쿠폰발급받았는지 저장
    @Transactional
    public CouponIssue saveCouponIssue(long couponId, long userId) {
        checkAlreadyIssuance(couponId, userId);

        CouponIssue issue = CouponIssue.builder()
            .couponId(couponId)
            .userId(userId)
            .build();

        return couponIssueJpaRepository.save(issue);
    }

    // TODO : 이미 발급받았는지 체크
    private void checkAlreadyIssuance(long couponId, long userId) {
        CouponIssue issue = couponIssueRepository.findFirstCouponIssue(couponId, userId);
        if(issue != null) {
            throw new CouponIssueException(DUPLICATE_COUPON_ISSUE, "이미 발급된 쿠폰입니다. user_id: %s, coupon_id: %s".formatted(userId, couponId));
        }
    }
}