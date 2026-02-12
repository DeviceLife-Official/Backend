package com.devicelife.devicelife_api.controller.recentview;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.recentview.dto.response.RecentlyViewedDeviceResponseDto;
import com.devicelife.devicelife_api.service.recentview.RecentlyViewedDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        - 최근 본 기기 기록 (검색 결과에서 모달 열 때 호출)
        
        ※ 기기 상세 조회(GET /api/devices/{deviceId}) 시에도 자동 저장됩니다.
        ※ 검색 결과 모달에서는 POST /api/recently-viewed/{deviceId}를 호출해주세요.
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

    @Operation(
            summary = "최근 본 기기 기록",
            description = """
            기기 검색 결과에서 모달을 열 때 호출하여 최근 본 기기로 기록한다.
            이미 조회한 기기는 조회 시간만 갱신된다.
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
                    description = "사용자 또는 기기를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/{deviceId}")
    public ResponseEntity<ApiResponse<Void>> recordDeviceView(
            @Parameter(description = "기기 ID", example = "1") @PathVariable Long deviceId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        recentlyViewedDeviceService.recordDeviceView(customUserDetails.getId(), deviceId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.RECENTLY_VIEWED_RECORD_SUCCESS.getCode(),
                        SuccessCode.RECENTLY_VIEWED_RECORD_SUCCESS.getMessage(),
                        null
                )
        );
    }
}
