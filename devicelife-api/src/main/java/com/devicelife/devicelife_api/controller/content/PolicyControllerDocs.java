package com.devicelife.devicelife_api.controller.content;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.content.dto.req.PolicySaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.PolicyDetailResponseDto;
import com.devicelife.devicelife_api.domain.content.dto.res.PolicyListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(
        name = "Policy",
        description = """
        약관 API. by 채정원
        """
)
public interface PolicyControllerDocs {

    @Operation(
            summary = "약관 등록",
            description = """
            새로운 약관을 등록합니다.

            - policyType: 약관 타입 (필수, 30자 이하) - 예: "TERMS", "PRIVACY", "MARKETING"
            - version: 버전 (필수, 30자 이하) - 예: "1.0.0", "1.2.0"
            - body: 본문 (필수, TEXT)
            - isActive: 활성화 여부 (선택, 기본값: false)

            **publishedAt 자동 관리:**
            - isActive=true: publishedAt 현재 시간으로 설정
            - isActive=false: publishedAt=null

            [프론트 참고]
            - 보통 비활성(isActive=false)으로 등록 후, 검토 완료되면 수정 API로 활성화합니다.
            - 같은 policyType에 여러 버전이 존재할 수 있습니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "등록 성공 (ApiResponse.result에 PolicyDetailResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패 (필수 필드 누락 또는 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<PolicyDetailResponseDto> createPolicy(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "약관 등록 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PolicySaveRequestDto.class))
            )
            @Valid @RequestBody PolicySaveRequestDto request
    );

    @Operation(
            summary = "약관 수정",
            description = """
            기존 약관을 수정합니다.

            - policyType: 약관 타입 (필수, 30자 이하)
            - version: 버전 (필수, 30자 이하)
            - body: 본문 (필수, TEXT)
            - isActive: 활성화 여부 (선택, 미입력 시 기존 값 유지)

            **publishedAt 자동 관리:**
            - isActive false→true: publishedAt 현재 시간으로 설정
            - isActive true→false: publishedAt=null
            - isActive 미입력: 기존 값 유지

            [프론트 참고]
            - 전체 필드를 모두 보내야 합니다 (PUT 방식).
            - isActive를 보내지 않으면 기존 값이 유지됩니다.
            - 버전 업데이트 시 version 필드를 변경하세요.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수정 성공 (ApiResponse.result에 PolicyDetailResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패 (필수 필드 누락 또는 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "약관을 찾을 수 없습니다. (POLICY_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<PolicyDetailResponseDto> updatePolicy(
            @Parameter(
                    description = "약관 ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long policyId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "약관 수정 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PolicySaveRequestDto.class))
            )
            @Valid @RequestBody PolicySaveRequestDto request
    );

    @Operation(
            summary = "약관 목록 조회",
            description = """
            약관 목록을 페이징하여 조회합니다.

            **요청 파라미터:**
            - policyType: 약관 타입 필터 (선택) - 예: "TERMS", "PRIVACY"
              - 없으면: 모든 타입 조회
              - 있으면: 해당 타입만 조회
            - isActive: 활성화 여부 (선택, 기본값: true)
            - page: 페이지 번호, 0부터 시작 (기본값: 0)
            - size: 페이지 크기 (기본값: 20)
            - sort: 정렬 기준 (기본값: version,desc)

            **응답 구조 (PolicyListDto):**
            - policies: 약관 목록 (PolicyResponseDto 배열)
            - listSize: 현재 페이지의 요소 개수
            - totalPage: 전체 페이지 수
            - totalElements: 전체 약관 개수
            - isFirst: 첫 페이지 여부
            - isLast: 마지막 페이지 여부

            **정렬 예시:**
            - 기본 (버전 내림차순): GET /api/policies?page=0&size=20
            - 버전 오름차순: GET /api/policies?page=0&size=20&sort=version,asc
            - 타입별 그룹화: GET /api/policies?page=0&size=20&sort=policyType,asc&sort=version,desc
            - 특정 타입만: GET /api/policies?policyType=TERMS&isActive=true

            [프론트 참고]
            - 활성화된 약관만 기본으로 조회됩니다.
            - 최신 버전이 먼저 표시됩니다 (version 내림차순).
            - **page는 0부터 시작합니다** (0: 첫 페이지, 1: 두 번째 페이지)
            - sort 파라미터로 정렬 순서를 자유롭게 변경할 수 있습니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공 (ApiResponse.result에 PolicyListDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<PolicyListDto> getPolicies(
            @Parameter(
                    description = "약관 타입 필터 (선택) - 예: TERMS, PRIVACY",
                    example = "TERMS",
                    required = false
            )
            @RequestParam(required = false) String policyType,

            @Parameter(
                    description = "활성화 여부 필터 (true: 활성, false: 비활성)",
                    example = "true",
                    required = false
            )
            @RequestParam(required = false, defaultValue = "true") Boolean isActive,

            @Parameter(hidden = true)
            @PageableDefault(size = 20, sort = "version", direction = Sort.Direction.DESC)
            Pageable pageable
    );

    @Operation(
            summary = "약관 상세 조회",
            description = """
            policyId로 약관 상세 정보를 조회합니다.

            - policyId로 특정 약관의 전체 정보 조회
            - 본문(body) 내용 포함

            **응답 필드:**
            - policyId: 약관 ID
            - policyType: 약관 타입 (예: TERMS, PRIVACY)
            - version: 버전 (예: 1.0.0)
            - body: 약관 본문 전체
            - publishedAt: 게시일 (활성화된 시점)
            - isActive: 활성화 여부

            [프론트 참고]
            - 사용자에게 약관 전문을 보여줄 때 사용합니다.
            - 회원가입 시 "전체보기" 버튼 클릭 시 이 API를 호출하세요.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공 (ApiResponse.result에 PolicyDetailResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "약관을 찾을 수 없습니다. (POLICY_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<PolicyDetailResponseDto> getPolicyById(
            @Parameter(
                    description = "약관 ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long policyId
    );

    @Operation(
            summary = "약관 영구 삭제",
            description = """
            약관을 영구적으로 삭제합니다.

            [주의]
            - 삭제된 데이터는 복구할 수 없습니다.
            - DB에서 완전히 제거됩니다 (Hard Delete).

            [프론트 참고]
            - 삭제 전 확인 팝업을 반드시 표시해야 합니다.
            - "정말로 이 약관을 삭제하시겠습니까?" 등의 메시지 권장
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공 (ApiResponse.result는 null)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "약관을 찾을 수 없습니다. (POLICY_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<Void> deletePolicy(
            @Parameter(
                    description = "약관 ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long policyId
    );
}
