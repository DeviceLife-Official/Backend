package com.devicelife.devicelife_api.controller.content;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.content.InquiryStatus;
import com.devicelife.devicelife_api.domain.content.dto.req.InquirySaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.req.InquiryStatusUpdateDto;
import com.devicelife.devicelife_api.domain.content.dto.res.InquiryListDto;
import com.devicelife.devicelife_api.domain.content.dto.res.InquiryDetailResponseDto;
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
        name = "Inquiry",
        description = """
        문의 API.
        """
)
public interface InquiryControllerDocs {

    @Operation(
            summary = "문의 등록",
            description = """
            새로운 문의를 등록합니다.

            - email: 이메일 (필수, 이메일 형식, 255자 이하)
            - subject: 주제 (필수, 200자 이하)
            - message: 본문 (필수)

            **status 자동 설정:**
            - 등록 시 status는 자동으로 OPEN으로 설정됩니다.

            [프론트 참고]
            - 현재는 userId를 path parameter로 받지만, 추후 JWT 토큰에서 추출하도록 변경 예정입니다.
            - 문의 등록 후 수정은 불가능합니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "등록 성공 (ApiResponse.result에 InquiryResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패 (이메일 형식 오류, 필수 필드 누락 등)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없습니다. (USER_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<InquiryDetailResponseDto> createInquiry(
            @Parameter(
                    description = "사용자 ID (1 이상의 양수, 추후 JWT로 대체 예정)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long userId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "문의 등록 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = InquirySaveRequestDto.class))
            )
            @Valid @RequestBody InquirySaveRequestDto request
    );

    @Operation(
            summary = "문의 상태 변경",
            description = """
            문의 상태를 변경합니다 (관리자 전용).

            - status: 변경할 상태 (필수)
              - OPEN: 접수됨
              - IN_PROGRESS: 처리 중
              - CLOSED: 종료됨

            [프론트 참고]
            - 관리자만 상태를 변경할 수 있습니다 (추후 권한 검증 추가 예정).
            - 상태 변경 시 문의 내용은 수정되지 않습니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "상태 변경 성공 (ApiResponse.result에 InquiryResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패 (상태 누락 또는 잘못된 값)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "문의를 찾을 수 없습니다. (INQUIRY_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<InquiryDetailResponseDto> updateInquiryStatus(
            @Parameter(
                    description = "문의 ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long inquiryId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "문의 상태 변경 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = InquiryStatusUpdateDto.class))
            )
            @Valid @RequestBody InquiryStatusUpdateDto request
    );

    @Operation(
            summary = "내 문의 목록 조회",
            description = """
            사용자가 작성한 문의 목록을 페이징하여 조회합니다.

            **요청 파라미터:**
            - status: 문의 상태 필터 (선택)
              - 없으면: 모든 상태의 문의 조회
              - 있으면: 해당 상태의 문의만 조회
              - 값: OPEN, IN_PROGRESS, CLOSED
            - page: 페이지 번호, 0부터 시작 (기본값: 0)
            - size: 페이지 크기 (기본값: 20)
            - sort: 정렬 기준 (기본값: createdAt,desc)

            **응답 구조 (InquiryListDto):**
            - inquiries: 문의 목록 (InquiryResponseDto 배열)
            - listSize: 현재 페이지의 요소 개수
            - totalPage: 전체 페이지 수
            - totalElements: 전체 문의 개수
            - isFirst: 첫 페이지 여부
            - isLast: 마지막 페이지 여부

            [프론트 참고]
            - 현재는 userId를 path parameter로 받지만, 추후 JWT 토큰에서 추출하도록 변경 예정입니다.
            - 최신순으로 정렬되어 반환됩니다.
            - **page는 0부터 시작합니다** (0: 첫 페이지, 1: 두 번째 페이지)
            - 요청 예시: GET /api/inquiries/my/1?page=0&size=20&status=OPEN
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공 (ApiResponse.result에 InquiryListDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없습니다. (USER_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<InquiryListDto> getMyInquiries(
            @Parameter(
                    description = "사용자 ID (1 이상의 양수, 추후 JWT로 대체 예정)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long userId,

            @Parameter(
                    description = "문의 상태 필터 (선택) - 예: OPEN, IN_PROGRESS, CLOSED",
                    example = "OPEN",
                    required = false
            )
            @RequestParam(required = false) InquiryStatus status,

            @Parameter(hidden = true)
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    );

    @Operation(
            summary = "모든 문의 목록 조회 (관리자 전용)",
            description = """
            모든 사용자의 문의 목록을 페이징하여 조회합니다 (관리자 전용).

            **요청 파라미터:**
            - status: 문의 상태 필터 (선택)
              - **없으면: OPEN, IN_PROGRESS 상태만 조회 (기본값)**
              - 있으면: 해당 상태의 문의만 조회
              - 값: OPEN, IN_PROGRESS, CLOSED
            - page: 페이지 번호, 0부터 시작 (기본값: 0)
            - size: 페이지 크기 (기본값: 20)
            - sort: 정렬 기준 (기본값: createdAt,desc)

            **응답 구조 (InquiryListDto):**
            - inquiries: 문의 목록 (InquiryResponseDto 배열)
            - listSize: 현재 페이지의 요소 개수
            - totalPage: 전체 페이지 수
            - totalElements: 전체 문의 개수
            - isFirst: 첫 페이지 여부
            - isLast: 마지막 페이지 여부

            **정렬 예시:**
            - 기본 (최신순): GET /api/inquiries/admin?page=0&size=20
            - 특정 상태만: GET /api/inquiries/admin?status=CLOSED
            - 모든 상태: status를 명시적으로 지정하여 각각 조회

            [프론트 참고]
            - 관리자만 접근 가능합니다 (추후 권한 검증 추가 예정).
            - status 파라미터 없이 요청하면 처리가 필요한 문의(OPEN, IN_PROGRESS)만 표시됩니다.
            - 종료된 문의를 보려면 status=CLOSED를 명시해야 합니다.
            - **page는 0부터 시작합니다** (0: 첫 페이지, 1: 두 번째 페이지)
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공 (ApiResponse.result에 InquiryListDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<InquiryListDto> getAllInquiries(
            @Parameter(
                    description = "문의 상태 필터 (선택, 없으면 OPEN+IN_PROGRESS) - 예: OPEN, IN_PROGRESS, CLOSED",
                    example = "CLOSED",
                    required = false
            )
            @RequestParam(required = false) InquiryStatus status,

            @Parameter(hidden = true)
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    );

    @Operation(
            summary = "문의 상세 조회",
            description = """
            특정 문의의 상세 정보를 조회합니다.

            **권한:**
            - 본인이 작성한 문의만 조회 가능합니다.
            - 관리자는 모든 문의를 조회할 수 있습니다.

            **응답 구조 (InquiryDetailResponseDto):**
            - inquiryId: 문의 ID
            - userId: 작성자 ID
            - email: 작성자 이메일
            - subject: 주제
            - message: 본문 내용
            - status: 문의 상태 (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
            - createdAt: 생성 일시
            - updatedAt: 수정 일시

            [프론트 참고]
            - 현재는 userId를 path parameter로 받지만, 추후 JWT 토큰에서 추출하도록 변경 예정입니다.
            - 권한이 없는 사용자가 조회 시도하면 403 에러가 반환됩니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공 (ApiResponse.result에 InquiryDetailResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "권한이 없습니다. 본인 또는 관리자만 조회 가능 (AUTH_4031)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "문의를 찾을 수 없습니다. (INQUIRY_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<InquiryDetailResponseDto> getInquiryDetail(
            @Parameter(
                    description = "문의 ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long inquiryId,

            @Parameter(
                    description = "사용자 ID (1 이상의 양수, 추후 JWT로 대체 예정)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long userId
    );
}
