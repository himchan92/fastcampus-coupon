package com.example.couponapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(value = NON_NULL) //null 응답은 json 에서 제외
public record CouponIssueResponseDto(boolean isSuccess, String comment) {
}