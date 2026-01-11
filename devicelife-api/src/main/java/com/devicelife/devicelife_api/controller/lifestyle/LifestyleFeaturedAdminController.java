package com.devicelife.devicelife_api.controller.lifestyle;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.lifestyle.request.SetFeaturedRequest;
import com.devicelife.devicelife_api.service.lifestyle.LifestyleFeaturedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/lifestyle")
public class LifestyleFeaturedAdminController {

    private final LifestyleFeaturedService service;

    @Value("${internal.admin-token:}")
    private String expectedToken;

    @PostMapping("/featured")
    public ApiResponse<Void> setFeatured(
            @RequestHeader(value = "X-Internal-Token", required = false) String token,
            @Valid @RequestBody SetFeaturedRequest req
    ) {
        if (expectedToken == null || expectedToken.isBlank()) {
            throw new CustomException(ErrorCode.SERVER_5001, "internal.admin-token 설정이 비어있습니다.");
        }
        if (token == null || !token.equals(expectedToken)) {
            throw new CustomException(ErrorCode.AUTH_4031, "토큰 불일치");
        }

        service.setFeatured(req.getTagKey(), req.getDeviceId1(), req.getDeviceId2(), req.getDeviceId3());
        return ApiResponse.success("LIFESTYLE_2002", "라이프스타일 대표 조합 설정에 성공했습니다.", null);
    }
}