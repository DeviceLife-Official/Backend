package com.devicelife.devicelife_api.controller.combo;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboCreateRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.response.ComboCreateResponseDto;
import com.devicelife.devicelife_api.service.combo.ComboService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Combo",
        description = """
        온보딩 '조합 생성하기' 화면에서 조합명(comboName)을 저장하는 API.
        """
)

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/combos")
public class ComboController {

    private final ComboService comboService;

    @Operation(
            summary = "조합 생성",
            description = """
            comboName을 받아 combos 테이블에 조합을 생성한다.

            [JWT 전환 예정]
            - 현재: userId를 요청 바디로 받음
            - 추후: JWT 인증 도입 시 userId는 토큰에서 추출하도록 변경
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 (ApiResponse.result에 comboId, comboName 반환)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 userId",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ComboCreateResponseDto>> createCombo(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = """
                    - userId: 임시(인증 도입 후 토큰에서 추출하도록 변경)
                    - comboName: 최대 80자(Combo 엔티티 제약)
                    """,
                    content = @Content(schema = @Schema(implementation = ComboCreateRequestDto.class))
            )
            @Valid @RequestBody ComboCreateRequestDto request) {
        ComboCreateResponseDto result = comboService.createCombo(request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_CREATE_SUCCESS.getCode(),
                        SuccessCode.COMBO_CREATE_SUCCESS.getMessage(),
                        result
                )
        );
    }
}