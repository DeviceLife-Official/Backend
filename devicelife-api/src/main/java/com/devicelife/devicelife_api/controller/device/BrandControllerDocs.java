package com.devicelife.devicelife_api.controller.device;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.device.dto.response.BrandResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Brand", description = "브랜드 조회 API")
public interface BrandControllerDocs {

    @Operation(summary = "브랜드 목록 조회", description = """
            브랜드 목록을 조회합니다.

            **사용 방법:**
            - deviceType 미지정: 전체 브랜드 목록 반환
            - deviceType 지정: 해당 기기 타입에 등록된 브랜드만 반환

            **예시:**
            - GET /api/brands → 전체 브랜드
            - GET /api/brands?deviceType=SMARTPHONE → 스마트폰 브랜드만 (Apple, Samsung 등)
            - GET /api/brands?deviceType=LAPTOP → 노트북 브랜드만
            """)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BrandResponseDto.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 기기 타입")
    ApiResponse<List<BrandResponseDto>> getAllBrands(
            @Parameter(description = "기기 타입 (SMARTPHONE, LAPTOP, TABLET, SMARTWATCH, AUDIO, KEYBOARD, MOUSE, CHARGER)")
            @RequestParam(required = false) DeviceType deviceType);
}
