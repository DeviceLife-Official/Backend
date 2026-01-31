package com.devicelife.devicelife_api.controller.device;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceSearchResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Device", description = "기기 검색 및 조회 API")
public interface DeviceControllerDocs {

    @Operation(summary = "기기 검색 (무한 스크롤)", description = """
            카테고리, 가격대, 브랜드로 필터링하여 기기 목록을 조회합니다.
            커서 기반 페이지네이션을 지원하며, 로그인 없이 접근 가능합니다.

            **필터링 조건:**
            - deviceTypes: 조회할 기기 타입들 (SMARTPHONE, LAPTOP, TABLET 등)
            - minPrice/maxPrice: 가격 범위 (원 단위)
            - brandIds: 브랜드명 리스트 (예: Samsung, Apple) -> (수정: 현재 ID로 받음, 추후 Name으로 변경 시 반영 필요)

            **페이지네이션:**
            - size: 한 페이지당 데이터 개수 (기본 24, 최대 60)
            - cursor: 다음 페이지 조회를 위한 커서 (첫 페이지는 null)
            """)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = DeviceSearchResponseDto.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 커서 등)")
    ApiResponse<DeviceSearchResponseDto> searchDevices(
            @Parameter(description = "페이지네이션 커서", example = "123") @RequestParam(required = false) String cursor,

            @Parameter(description = "페이지 크기", example = "24") @RequestParam(required = false) Integer size,

            @Parameter(description = "기기 타입 목록", example = "SMARTPHONE,LAPTOP") @RequestParam(required = false) List<DeviceType> deviceTypes,

            @Parameter(description = "최소 가격", example = "500000") @RequestParam(required = false) Integer minPrice,

            @Parameter(description = "최대 가격", example = "1500000") @RequestParam(required = false) Integer maxPrice,

            @Parameter(description = "브랜드 ID 목록", example = "1,2") @RequestParam(required = false) List<Long> brandIds);
}
