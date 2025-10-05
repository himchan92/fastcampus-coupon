package com.fastcampus.couponcore.model;

import static com.fastcampus.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_DATE;
import static com.fastcampus.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;

import com.fastcampus.couponcore.exception.CouponIssueException;
import com.fastcampus.couponcore.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "coupons")
public class Coupon
    extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    private Integer totalQuantity;

    @Column(nullable = false)
    private int issuedQuantity;

    @Column(nullable = false)
    private int discountAmount;

    @Column(nullable = false)
    private int minAvailableAmount;

    @Column(nullable = false)
    private LocalDateTime dateIssueStart;

    @Column(nullable = false)
    private LocalDateTime dateIssueEnd;

    //=======검증메소드=======

    //발급수량
    public boolean availableIssueQuantity() {
        if(totalQuantity == null) {
            return true;
        }
        return totalQuantity > issuedQuantity;
    }

    //발급기한
    public boolean availableIssueDate() {
        LocalDateTime now = LocalDateTime.now();
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
    }

    public void issue() {
        //수량검증
        if(!availableIssueQuantity()) {
            throw new CouponIssueException(INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과합니다. total : %s, issued: %s".formatted(totalQuantity, issuedQuantity)); //예외는 커스텀예외 권장
        }

        //기한검증
        if(!availableIssueDate()) {
            throw new CouponIssueException(INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. request : %s, issueStart : %s, issueEnd : %s".formatted(LocalDateTime.now(), dateIssueStart, dateIssueEnd)); //예외는 커스텀예외 권장
        }

        //검증 다통과되어야 수행
        issuedQuantity++;
    }
}