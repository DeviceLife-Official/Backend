package com.devicelife.devicelife_api.controller.content;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.content.dto.req.FaqSaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.FaqResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(
        name = "FAQ",
        description = """
        자주 묻는 질문(FAQ) API. by 채정원
        """
)
public interface FaqControllerDocs {

    @Operation(
            summary = "FAQ 등록",
            description = """
            새로운 FAQ를 등록합니다.

            - question: 질문 (필수, 255자 이하)
            - answer: 답변 (필수)
            - sortOrder: 정렬 순서 (선택, 기본값: 0)
            - isPublished: 게시 여부 (선택, 기본값: false)

            [프론트 참고]
            - sortOrder 값이 작을수록 상단에 표시됩니다.
            - 비공개로 작성 후 나중에 게시 가능합니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "등록 성공 (ApiResponse.result에 FaqResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패 (질문/답변 누락 또는 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<FaqResponseDto> createFaq(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "FAQ 등록 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FaqSaveRequestDto.class))
            )
            @Valid @RequestBody FaqSaveRequestDto request
    );

    @Operation(
            summary = "FAQ 전체 목록 조회",
            description = """
            FAQ 전체 목록을 조회합니다.

            **요청 파라미터:**
            - isPublished: 게시 여부 필터 (기본값: true)

            **응답:**
            - sortOrder 오름차순으로 정렬된 FAQ 목록 반환
            - List<FaqResponseDto> 형식

            [프론트 참고]
            - 게시된 FAQ만 기본으로 조회됩니다.
            - sortOrder가 작은 값부터 순서대로 표시됩니다.
            - 페이징 없이 전체 목록이 반환됩니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공 (ApiResponse.result에 List<FaqResponseDto> 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<List<FaqResponseDto>> getFaqs(
            @Parameter(
                    description = "게시 여부 필터 (true: 게시됨, false: 비공개)",
                    example = "true",
                    required = false
            )
            @RequestParam(required = false, defaultValue = "true") Boolean isPublished
    );

    @Operation(
            summary = "FAQ 수정",
            description = """
            기존 FAQ를 수정합니다.

            - question: 질문 (필수, 255자 이하)
            - answer: 답변 (필수)
            - sortOrder: 정렬 순서 (선택, 미입력 시 기존 값 유지)
            - isPublished: 게시 여부 (선택, 미입력 시 기존 값 유지)

            [프론트 참고]
            - 전체 필드를 모두 보내야 합니다 (PUT 방식).
            - sortOrder나 isPublished를 보내지 않으면 기존 값이 유지됩니다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수정 성공 (ApiResponse.result에 FaqResponseDto 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패 (질문/답변 누락 또는 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "FAQ를 찾을 수 없습니다. (FAQ_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<FaqResponseDto> updateFaq(
            @Parameter(
                    description = "FAQ ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long faqId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "FAQ 수정 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FaqSaveRequestDto.class))
            )
            @Valid @RequestBody FaqSaveRequestDto request
    );

    @Operation(
            summary = "FAQ 영구 삭제",
            description = """
            FAQ를 영구적으로 삭제합니다.

            [주의]
            - 삭제된 데이터는 복구할 수 없습니다.
            - DB에서 완전히 제거됩니다 (Hard Delete).

            [프론트 참고]
            - 삭제 전 확인 팝업을 반드시 표시해야 합니다.
            - 예: "정말 삭제하시겠습니까? 삭제된 FAQ는 복구할 수 없습니다."
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
                    description = "FAQ를 찾을 수 없습니다. (FAQ_4041)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ApiResponse<Void> deleteFaq(
            @Parameter(
                    description = "FAQ ID (1 이상의 양수)",
                    example = "1",
                    required = true
            )
            @PathVariable @Positive Long faqId
    );
}
