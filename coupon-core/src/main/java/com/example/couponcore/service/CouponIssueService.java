package com.example.couponcore.service;

import static com.example.couponcore.exception.ErrorCode.COUPON_NOT_EXIST;
import static com.example.couponcore.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.example.couponcore.repository.mysql.CouponIssueRepository;
import com.example.couponcore.repository.mysql.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

  private final CouponJpaRepository couponJpaRepository;
  private final CouponIssueJpaRepository couponIssueJpaRepository;
  private final CouponIssueRepository couponIssueRepository;

  @Transactional
  public void issue(long couponId, long userId) {
    //쿠폰실제로 존재하는지 체크위해 조회
    Coupon coupon = findCoupon(couponId);
    coupon.issue(); //발급가능수량, 일자 검증수행
    saveCouponIssue(couponId, userId);

  }

  @Transactional(readOnly = true)
  public Coupon findCoupon(long couponId) {
    return couponJpaRepository.findById(couponId).orElseThrow(() -> {
      throw new CouponIssueException(COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. %s".formatted(couponId));
    });
  }

  @Transactional
  public CouponIssue saveCouponIssue(long couponId, long userId) {
    checkAllreadyIssuance(couponId, userId); //저장전 검증수행

    CouponIssue issue = CouponIssue.builder()
        .couponId(couponId)
        .userId(userId)
        .build();

    return couponIssueJpaRepository.save(issue);
  }

  //이미 쿠폰발급되었는지 체크
  private void checkAllreadyIssuance(long couponId, long userId) {
    CouponIssue issue = couponIssueRepository.findFirstCouponIssue(couponId, userId);

    if(issue != null) { //이미 발급된경우 발급막기
      throw new CouponIssueException(DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰입니다. user_id: %s, couponId: %s".formatted(userId, couponId));
    }
  }
}
