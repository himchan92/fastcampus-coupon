package com.example.couponcore.model;

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
      throw new RuntimeException("수량 검증");
    }
    //발급기한 실패시
    if(!availableIssueDate()) {
      throw new RuntimeException("기한 검증");
    }
    //다 통과시 수량증가
    issuedQuantity++; //외부이슈누를시 발급수량증가
  }
}
