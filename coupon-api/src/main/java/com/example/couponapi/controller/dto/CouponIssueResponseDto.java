package com.example.couponapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL) //NULL은 JSON 응답에서 제외
public record CouponIssueResponseDto(boolean isSuccess, String comment) {
}
