package com.fastcampus.couponcore.service;

import static com.fastcampus.couponcore.exception.ErrorCode.DUPLICATE_COUPON_ISSUE;
import static com.fastcampus.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;
import static com.fastcampus.couponcore.util.CouponRedisUtils.getIssueRequestKey;

import com.fastcampus.couponcore.exception.CouponIssueException;
import com.fastcampus.couponcore.repository.redis.RedisRepository;
import com.fastcampus.couponcore.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueRedisService {

    private final RedisRepository redisRepository;

    public void checkCouponIssueQuantity(CouponRedisEntity coupon, long userId) {
        //발급수량검증
        if(!availableTotalIssueQuantity(coupon.totalQuantity(), coupon.id())) {
            throw new CouponIssueException(INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과합니다. couponId: %s, userId: %s".formatted(coupon.id(), userId));
        }

        //중복발급 검증
        if(!availableUserIssueQuantity(coupon.id(), userId)) {
            throw new CouponIssueException(DUPLICATE_COUPON_ISSUE, "이미 발급 요청이 처리되었습니다. couponId: %s, userId: %s".formatted(coupon.id(), userId));
        }
    }

    //TODO: 요청수가 총건수를 안넘는지 검증, 총건수없으면 스킵
    public boolean availableTotalIssueQuantity(Integer totalQuantity, long couponId) {
        if(totalQuantity == null) {
            return true;
        }
        String key = getIssueRequestKey(couponId);
        return totalQuantity > redisRepository.sCard(key);
    }

    public boolean availableUserIssueQuantity(long couponId, long userId) {
        String key = getIssueRequestKey(couponId);
        return !redisRepository.sIsMember(key, String.valueOf(userId));
    }
}
