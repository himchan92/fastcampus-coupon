package com.example.couponcore.model;

import static com.example.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_DATE;
import static com.example.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
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
public class Coupon extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CouponType couponType;

  private Integer totalQuantity; //수량남아있는지 체크위한 전체수량

  @Column(nullable = false)
  private int issuedQuantity;

  @Column(nullable = false)
  private int discountAmount;

  @Column(nullable = false)
  private int minAvailableAmount;

  @Column(nullable = false)
  private LocalDateTime dateIssueStart;

  @Column(nullable = false)
  private LocalDateTime dateIssueEnd; //발급마감일기준 발급되는지 체크

  //발급가능여부
  public boolean availableIssueQuantity() {
    if(totalQuantity == null) { //총수량없음 제한없으니 통과
      return true;
    }
    return totalQuantity > issuedQuantity;
  }

  //발급기한체크
  public boolean availableIssueDate() {
    LocalDateTime now = LocalDateTime.now();
    return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
  }

  public void issue() {
    //수량검증 실패시
    if(!availableIssueQuantity()) {
      throw new CouponIssueException(INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과합니다. total : %s, issued: %s".formatted(totalQuantity, issuedQuantity));
    }
    //발급기한 실패시
    if(!availableIssueDate()) {
      throw new CouponIssueException(INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. request : %s, issueStart : %s, issueEnd: %s".formatted(LocalDateTime.now(), dateIssueStart, dateIssueEnd));
    }
    //다 통과시 수량증가
    issuedQuantity++; //외부이슈누를시 발급수량증가
  }
}
