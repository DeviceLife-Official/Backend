package com.devicelife.devicelife_api.controller.content;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.content.dto.req.NoticeSaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.NoticeDetailResponseDto;
import com.devicelife.devicelife_api.domain.content.dto.res.NoticeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(
        name = "Notice",
        description = """
        공지사항 API.
        """
)
public interface NoticeControllerDocs {

    @Operation(
            summary = "공지사항 목록 조회",
            description = """
            공지사항 목록을 페이징하여 조회합니다.

            - isPublished: 게시 여부 필터 (기본값: true)
            - sort: 정렬 기준 (기본값: createdAt,desc)
            - page: 페이지 번호 (기본값: 0)
            - size: 페이지 크기 (기본값: 20)

            [프론트 참고]
            - 게시된 공지사항만 기본으로 조회됩니다.
            - 최신순으로 정렬되어 반환됩니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공 (ApiResponse.result에 Page<NoticeResponseDto> 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<Page<NoticeResponseDto>> getNotices(
            @Parameter(
                    description = "게시 여부 필터 (true: 게시됨, false: 비공개)",
                    example = "true",
                    required = false
            )
            @RequestParam(required = false, defaultValue = "true") Boolean isPublished,

            @Parameter(hidden = true)
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    );

    @Operation(
            summary = "공지사항 상세 조회",
            description = """
            noticeId로 공지사항 상세 정보를 조회합니다.

            - 제목, 본문, 게시 여부, 게시일, 생성일, 수정일 정보를 반환합니다.

            [프론트 참고]
            - 목록 조회와 달리 본문(body) 내용이 포함됩니다.
            - createdAt, updatedAt 정보도 함께 제공됩니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공 (ApiResponse.result에 NoticeDetailResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "공지를 찾을 수 없습니다. (NOTICE_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<NoticeDetailResponseDto> getNotice(
            @Parameter(
                    description = "공지 ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long noticeId
    );

    @Operation(
            summary = "공지사항 등록",
            description = """
            새로운 공지사항을 등록합니다.

            - title: 제목 (필수, 200자 이하)
            - body: 본문 (필수)
            - isPublished: 게시 여부 (필수, true/false)
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "등록 성공 (ApiResponse.result에 NoticeDetailResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패 (제목/본문 누락 또는 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<NoticeDetailResponseDto> createNotice(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "공지 등록 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NoticeSaveRequestDto.class))
            )
            @Valid @RequestBody NoticeSaveRequestDto request
    );

    @Operation(
            summary = "공지사항 수정",
            description = """
            기존 공지사항을 수정합니다.

            - title: 제목 (필수, 200자 이하)
            - body: 본문 (필수)
            - isPublished: 게시 여부 (필수, true/false)

            [프론트 참고]
            - 전체 필드를 모두 보내야 합니다 (PUT 방식).
            - 비공개로 작성 후 나중에 게시하는 워크플로우에 적합합니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수정 성공 (ApiResponse.result에 NoticeDetailResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패 (제목/본문 누락 또는 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "공지를 찾을 수 없습니다. (NOTICE_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<NoticeDetailResponseDto> updateNotice(
            @Parameter(
                    description = "공지 ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long noticeId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "공지 수정 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NoticeSaveRequestDto.class))
            )
            @Valid @RequestBody NoticeSaveRequestDto request
    );

    @Operation(
            summary = "공지사항 영구 삭제",
            description = """
            공지사항을 영구적으로 삭제합니다.

            [주의]
            - 삭제된 데이터는 복구할 수 없습니다.
            - DB에서 완전히 제거됩니다 (Hard Delete).

            [프론트 참고]
            - 삭제 전 확인 팝업을 반드시 표시해야 합니다.
            - 예: "정말 삭제하시겠습니까? 삭제된 공지는 복구할 수 없습니다."
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공 (ApiResponse.result = null)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "공지를 찾을 수 없습니다. (NOTICE_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<Void> deleteNotice(
            @Parameter(
                    description = "공지 ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long noticeId
    );
}
