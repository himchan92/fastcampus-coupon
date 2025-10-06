package com.fastcampus.couponconsumer.component;

import com.fastcampus.couponcore.repository.redis.RedisRepository;
import com.fastcampus.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class CouponIssueListener {

    private final RedisRepository redisRepository;
    private final CouponIssueService couponIssueService;

    @Scheduled(fixedDelay = 1000L)
    public void issue() {
        log.info("listen...");
    }
}
