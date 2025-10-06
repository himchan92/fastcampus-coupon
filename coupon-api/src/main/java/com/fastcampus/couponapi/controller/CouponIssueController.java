package com.fastcampus.couponapi.controller;

import com.fastcampus.couponapi.controller.dto.CouponIssueRequestDto;
import com.fastcampus.couponapi.controller.dto.CouponIssueResponseDto;
import com.fastcampus.couponapi.service.CouponIssueRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueRequestService couponIssueRequestService;

    // TODO: 쿠폰발급, 실패시 예외처리되며
    @PostMapping("/v1/issue")
    public CouponIssueResponseDto issueV1(@RequestBody CouponIssueRequestDto body) {
        couponIssueRequestService.issueRequestV1(body);
        return new CouponIssueResponseDto(true, null);
    }
}
