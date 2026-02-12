package com.devicelife.devicelife_api.controller.device;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceDetailResponseDto;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceSearchResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceSortType;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Device", description = "기기 검색 및 조회 API by 채정원")
public interface DeviceControllerDocs {

    @Operation(summary = "기기 검색 (무한 스크롤)", description = """
            카테고리, 가격대, 브랜드로 필터링하여 기기 목록을 조회합니다.
            커서 기반 페이지네이션을 지원하며, 로그인 없이 접근 가능합니다.

            **필터링 조건:**
            - deviceTypes: 조회할 기기 타입들 (SMARTPHONE, LAPTOP, TABLET 등)
            - minPrice/maxPrice: 가격 범위 (원 단위)
            - brandIds: 브랜드명 리스트 (예: Samsung, Apple) -> (수정: 현재 ID로 받음, 추후 Name으로 변경 시 반영 필요)

            **정렬:**
            - sortType: LATEST(최신순), NAME_ASC(가나다순), PRICE_ASC(낮은 가격순), PRICE_DESC(높은 가격순)

            **페이지네이션:**
            - size: 한 페이지당 데이터 개수 (기본 24, 최대 60)
            - cursor: 다음 페이지 조회를 위한 커서 (첫 페이지는 null)
            """)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = DeviceSearchResponseDto.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 커서 등)")
    ApiResponse<DeviceSearchResponseDto> searchDevices(
            @Parameter(description = "검색 키워드 (기기명)") @RequestParam(required = false) String keyword,

            @Parameter(description = "페이지네이션 커서") @RequestParam(required = false) String cursor,

            @Parameter(description = "페이지 크기 (기본 24, 최대 60)") @RequestParam(required = false) Integer size,

            @Parameter(description = "정렬 타입 (기본 LATEST)") @RequestParam(required = false, defaultValue = "LATEST") DeviceSortType sortType,

            @Parameter(description = "기기 타입 목록") @RequestParam(required = false) List<DeviceType> deviceTypes,

            @Parameter(description = "최소 가격") @RequestParam(required = false) Integer minPrice,

            @Parameter(description = "최대 가격") @RequestParam(required = false) Integer maxPrice,

            @Parameter(description = "브랜드 ID 목록") @RequestParam(required = false) List<Long> brandIds);

    @Operation(summary = "기기 세부 정보 조회", description = """
            기기의 상세 정보를 조회합니다.
            모달이 열릴 때 이 API를 호출하여 기기 정보를 표시합니다.
            
            로그인한 유저의 경우, 이 API를 호출하면 자동으로 최근 본 기기 테이블에 저장됩니다.
            비로그인 상태에서도 기기 정보 조회는 가능하지만, 최근 본 기기에는 저장되지 않습니다.
            
            반환 정보:
            - 모델명
            - 가격
            - 카테고리
            - 브랜드
            - 무게 (노트북: kg, 나머지: g / Smartphone·Smartwatch는 null)
            - 연결 단자 (기기 타입별로 다름)
            - 출시일
            - 태그 (현재는 빈 배열)
            
            빈 값 처리: 값이 없는 필드는 빈 문자열("")로 반환됩니다.
            무게 필드는 데이터가 없으면 응답에서 제외됩니다 (null → JSON 미포함).
            """)
    @SecurityRequirement(name = "JWT TOKEN")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = DeviceDetailResponseDto.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "기기를 찾을 수 없음")
    ApiResponse<DeviceDetailResponseDto> getDeviceDetail(
            @Parameter(description = "기기 ID") @PathVariable Long deviceId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    );
}
