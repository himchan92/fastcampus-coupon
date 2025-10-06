package com.fastcampus.couponcore.util;

public class CouponRedisUtils {

    //쿠폰 키값 생성 공통
    public static String getIssueRequestKey(long couponId) {
        return "issue.request.couponId=%s".formatted(couponId);
    }

    //쿠폰 큐 발급 공통
    public static String getIssueRequestQueueKey() {
        return "issue.request";
    }
}