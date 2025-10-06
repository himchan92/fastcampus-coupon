package com.fastcampus.couponapi.service;

import com.fastcampus.couponapi.controller.dto.CouponIssueRequestDto;
import com.fastcampus.couponcore.service.AsyncCouponIssueServiceV1;
import com.fastcampus.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService; //coupon core 참조
    private final AsyncCouponIssueServiceV1 asyncCouponIssueServiceV1;

    public void issueRequestV1(CouponIssueRequestDto requestDto) {
        synchronized (this) {
            couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        }
        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(requestDto.couponId(), requestDto.userId()));
    }

    public void asyncIssueRequestV1(CouponIssueRequestDto requestDto) {
        asyncCouponIssueServiceV1.issue(requestDto.couponId(), requestDto.userId());
    }
}
