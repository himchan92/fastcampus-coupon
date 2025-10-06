package com.fastcampus.couponapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//쿠폰발급응답값
@JsonInclude(value = Include.NON_NULL)
public record CouponIssueResponseDto(boolean isSuccess, String comment) {

}
