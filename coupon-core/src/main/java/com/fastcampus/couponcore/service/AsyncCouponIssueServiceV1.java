package com.fastcampus.couponcore.service;

import static com.fastcampus.couponcore.exception.ErrorCode.DUPLICATE_COUPON_ISSUE;
import static com.fastcampus.couponcore.exception.ErrorCode.FAIL_COUPON_ISSUE_REQUEST;
import static com.fastcampus.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_DATE;
import static com.fastcampus.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;
import static com.fastcampus.couponcore.util.CouponRedisUtils.getIssueRequestKey;
import static com.fastcampus.couponcore.util.CouponRedisUtils.getIssueRequestQueueKey;

import com.fastcampus.couponcore.exception.CouponIssueException;
import com.fastcampus.couponcore.exception.ErrorCode;
import com.fastcampus.couponcore.model.Coupon;
import com.fastcampus.couponcore.repository.redis.RedisRepository;
import com.fastcampus.couponcore.repository.redis.dto.CouponIssueRequest;
import com.fastcampus.couponcore.repository.redis.dto.CouponRedisEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncCouponIssueServiceV1 {

    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponIssueService couponIssueService;
    private final CouponCacheService couponCacheService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    //TODO : REDIS 방식 캐시적용한 쿠폰발급
    public void issue(long couponId, long userId) {
        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssueableCoupon();
        couponIssueRedisService.checkCouponIssueQuantity(coupon, userId);
        issueRequest(couponId, userId);
    }

    private void issueRequest(long couponId, long userId) {
        CouponIssueRequest issueRequest = new CouponIssueRequest(couponId, userId);

        try {
            String value = objectMapper.writeValueAsString(issueRequest);
            redisRepository.sAdd(getIssueRequestKey(couponId), String.valueOf(userId));
            redisRepository.rPush(getIssueRequestQueueKey(), value);
            // 쿠폰 발급 큐 적재
        } catch (JsonProcessingException e) {
            throw new CouponIssueException(FAIL_COUPON_ISSUE_REQUEST, "input: %s".formatted(issueRequest));
        }
    }
}