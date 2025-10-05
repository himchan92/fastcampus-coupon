package com.fastcampus.couponcore.repository.mysql;

import static com.fastcampus.couponcore.model.QCouponIssue.couponIssue;

import com.fastcampus.couponcore.model.CouponIssue;
import com.fastcampus.couponcore.model.QCouponIssue;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepository {

    private final JPQLQueryFactory queryFactory;

    public CouponIssue findFirstCouponIssue(long couponId, long userId) {
        return queryFactory.selectFrom(couponIssue)
            .where(couponIssue.couponId.eq(couponId))
            .where(couponIssue.userId.eq(userId))
            .fetchFirst();
    }
}
