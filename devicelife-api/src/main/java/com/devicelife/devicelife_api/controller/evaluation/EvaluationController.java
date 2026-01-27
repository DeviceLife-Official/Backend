package com.devicelife.devicelife_api.controller.evaluation;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.evaluation.dto.EvaluationDto;
import com.devicelife.devicelife_api.domain.evaluation.dto.EvaluationPayloadResDto;
import com.devicelife.devicelife_api.service.evaluation.EvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.devicelife.devicelife_api.common.response.SuccessCode.COMBO_LIST_SUCCESS;
import static com.devicelife.devicelife_api.common.response.SuccessCode.COMMON_2001;

@Tag(
        name = "Evaluation",
        description = """
        기기 조합 관리 API
        - 워커에 평가용 데이터 전달
        - 워커가 계산한 평가점수 갱신
        """
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @Operation(
            summary = "평가용 데이터",
            description = """
            PathVariable로 조합 id 첨부하면,
            워커가 계산에 필요한 최소 데이터만 가져감.
            (해당 조합에 담겨있는 기기들)
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
                    description = "존재하지 않는 combinationId",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{combinationId}/payload")
    public ApiResponse<EvaluationPayloadResDto> getData(@PathVariable Long combinationId) {

        return ApiResponse.success(
                COMBO_LIST_SUCCESS.getCode(),
                COMBO_LIST_SUCCESS.getMessage(),
                evaluationService.getDevicesData(combinationId));
    }

    @Operation(
            summary = "평가 점수 저장",
            description = """
            RequestBody에 평가 버전과 평가 점수 첨부,
            DB내 버전보다 높은 버전이 아닐경우 갱신 X (응답은 여전히 200 성공)
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
                    description = "존재하지 않는 combinationId",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/{combinationId}/result")
    public ApiResponse<Void> postEval(
            @PathVariable Long combinationId,
            @RequestBody EvaluationDto.postEvaluationReqDto dto) {
        evaluationService.postEvaluation(combinationId, dto);
        return ApiResponse.success(
                COMMON_2001.getCode(),
                COMMON_2001.getMessage(),
                null);
    }

}
