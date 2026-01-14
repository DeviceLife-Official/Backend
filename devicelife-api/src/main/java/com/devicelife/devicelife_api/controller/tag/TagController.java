package com.devicelife.devicelife_api.controller.tag;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.device.dto.request.UserTagRequestDto;
import com.devicelife.devicelife_api.domain.device.dto.response.TagResponseDto;
import com.devicelife.devicelife_api.service.tag.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Tag",
        description = """
        온보딩(관심사/라이프스타일/브랜드) 및 라이프스타일 화면에서 사용하는 태그 API.
        """
)

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(
            summary = "태그 목록 조회",
            description = """
            type으로 필터링해서 태그를 조회한다.

            - type 미지정: 전체 태그
            - type 지정: 해당 tagType만 조회 (예: INTEREST, LIFESTYLE, BRAND)

            [프론트 참고]
            - 화면 좌측 태그 버튼 렌더링에 사용.
            - type 값은 대소문자 섞여 와도 서버에서 upper-case normalize 처리.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 (ApiResponse.result에 TagResponseDto 리스트 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponseDto>>> getTags(
            @Parameter(
                    description = "태그 타입 필터. 없으면 전체 조회. (예: INTEREST, LIFESTYLE, BRAND)",
                    example = "LIFESTYLE",
                    required = false
            )
            @RequestParam(value = "type", required = false) String type
    ) {
        List<TagResponseDto> result = tagService.getTags(type);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.TAG_GET_SUCCESS.getCode(),
                        SuccessCode.TAG_GET_SUCCESS.getMessage(),
                        result
                )
        );
    }
    @Operation(
            summary = "유저 선택 태그 저장 (replace)",
            description = """
            유저가 선택한 tagIds를 userTags에 저장한다.

            [중요: replace(덮어쓰기) 동작]
            - 요청으로 들어온 tagIds를 조회해서, 그 태그들이 속한 tagType(들)을 구함
            - 해당 tagType(들)에 해당하는 기존 userTags를 삭제
            - 이번 요청의 tagIds를 다시 저장

            [왜 replace?]
            - 온보딩/선택 UI에서 유저가 선택을 바꾸는 게 자연스러움.
              add-only(추가만)로 저장하면 사용자가 '해제'한 태그가 DB에 남아 데이터가 망가짐.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 (ApiResponse.result = null)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "userId 또는 tagIds 중 존재하지 않는 값 포함",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/user")
    public ResponseEntity<ApiResponse<Void>> saveUserTags(
            @Valid @RequestBody UserTagRequestDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        tagService.saveUserTags(request,customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.TAG_SAVE_SUCCESS.getCode(),
                        SuccessCode.TAG_SAVE_SUCCESS.getMessage(),
                        null
                )
        );
    }
}
