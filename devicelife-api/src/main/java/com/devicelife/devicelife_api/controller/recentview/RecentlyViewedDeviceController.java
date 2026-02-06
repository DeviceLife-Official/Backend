package com.devicelife.devicelife_api.controller.recentview;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.recentview.dto.response.RecentlyViewedDeviceResponseDto;
import com.devicelife.devicelife_api.service.recentview.RecentlyViewedDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Recently Viewed Device",
        description = """
        by 이태훈
        최근 본 기기 API - 인증 필요 (JWT Token)
        - 최근 본 기기 목록 조회 (최신순, 상위 2개)
        
        ※ 기기 조회 시 자동 저장됩니다. (GET /api/devices/{deviceId} 호출 시)
        """
)
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT TOKEN")
@RequestMapping("/api/recently-viewed")
public class RecentlyViewedDeviceController {

    private final RecentlyViewedDeviceService recentlyViewedDeviceService;

    @Operation(
            summary = "최근 본 기기 목록 조회",
            description = """
            사용자가 최근에 조회한 기기 목록을 반환한다.
            최신순으로 정렬되며, 최대 2개의 기기를 반환한다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 정보가 누락되었거나 유효하지 않음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<RecentlyViewedDeviceResponseDto>>> getRecentlyViewedDevices(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<RecentlyViewedDeviceResponseDto> result = 
                recentlyViewedDeviceService.getRecentlyViewedDevices(customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.RECENTLY_VIEWED_GET_SUCCESS.getCode(),
                        SuccessCode.RECENTLY_VIEWED_GET_SUCCESS.getMessage(),
                        result
                )
        );
    }
}
