package com.devicelife.devicelife_api.controller.lifestyle;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.lifestyle.request.DeviceSearchDto;
import com.devicelife.devicelife_api.repository.lifestyle.DeviceSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/devices")
public class DeviceAdminController {

    private final DeviceSearchRepository deviceSearchRepository;

    @Value("${internal.admin-token:}")
    private String expectedToken;

    @GetMapping("/search")
    public ApiResponse<List<DeviceSearchDto>> search(
            @RequestHeader(value = "X-Internal-Token", required = false) String token,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (expectedToken == null || expectedToken.isBlank()) {
            throw new CustomException(ErrorCode.SERVER_5001, "internal.admin-token 설정이 비어있습니다.");
        }
        if (token == null || !token.equals(expectedToken)) {
            throw new CustomException(ErrorCode.AUTH_4031, "토큰 불일치");
        }

        int safeLimit = Math.min(Math.max(limit, 1), 30);
        List<DeviceSearchDto> result = deviceSearchRepository.searchByKeyword(keyword, safeLimit);

        return ApiResponse.success("DEVICE_2001", "디바이스 검색에 성공했습니다.", result);
    }
}