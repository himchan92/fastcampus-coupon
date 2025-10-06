package com.fastcampus.couponcore.repository.redis.dto;

import static com.fastcampus.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_DATE;

import com.fastcampus.couponcore.exception.CouponIssueException;
import com.fastcampus.couponcore.exception.ErrorCode;
import com.fastcampus.couponcore.model.Coupon;
import com.fastcampus.couponcore.model.CouponType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;

public record CouponRedisEntity(
    Long id,
    CouponType couponType,
    Integer totalQuantity,

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime dateIssueStart,

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime dateIssueEnd
) {
    // TODO: Coupon 엔티티로부터 생성
    public CouponRedisEntity(Coupon coupon) {
        this(
            coupon.getId(),
            coupon.getCouponType(),
            coupon.getTotalQuantity(),
            coupon.getDateIssueStart(),
            coupon.getDateIssueEnd()
        );
    }

    private boolean availableIssueDate() {
        LocalDateTime now = LocalDateTime.now();
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
    }

    public void checkIssueableCoupon() {
        if(!availableIssueDate()) {
            throw new CouponIssueException(INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. couponId: %s, issueStart: %s, issueEnd: %s".formatted(id, dateIssueStart, dateIssueEnd));
        }
    }
}
