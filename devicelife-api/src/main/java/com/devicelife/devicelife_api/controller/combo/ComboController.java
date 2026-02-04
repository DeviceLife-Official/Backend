package com.devicelife.devicelife_api.controller.combo;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboCreateRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboDeviceAddRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboPermanentDeleteRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboUpdateRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.response.*;
import com.devicelife.devicelife_api.scheduler.EvaluationScheduler;
import com.devicelife.devicelife_api.service.combo.ComboService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Combo",
        description = """
        기기 조합 관리 API - 인증 필요 (JWT Token) - by 이태훈, 박은서
        - 조합 생성/수정/조회/삭제
        - 조합에 기기 추가/삭제
        - 즐겨찾기(핀) 관리
        - 휴지통 관리 (목록/복구/영구삭제)
        """
)
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT TOKEN")
@RequestMapping("/api/combos")
public class ComboController {

    private final ComboService comboService;
    private final EvaluationScheduler evaluationScheduler;

    // ========== 조합 기본 CRUD ==========

    @Operation(
            summary = "조합 생성 by 박은서",
            description = """
            comboName을 받아 combos 테이블에 조합을 생성한다.
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
                    - comboName: 최대 80자(Combo 엔티티 제약)
                    """,
                    content = @Content(schema = @Schema(implementation = ComboCreateRequestDto.class))
            )
            @Valid @RequestBody ComboCreateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ComboCreateResponseDto result = comboService.createCombo(request,customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_CREATE_SUCCESS.getCode(),
                        SuccessCode.COMBO_CREATE_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합 상세 조회 - 이태훈",
            description = """
            특정 조합의 상세 정보를 조회한다.
            포함된 기기 목록도 함께 반환한다.
            본인의 조합만 조회할 수 있다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없음 (본인의 조합이 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{comboId}")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> getComboDetail(
            @PathVariable Long comboId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ComboDetailResponseDto result = comboService.getComboDetail(comboId, customUserDetails);
        // 2. [추가] "방금 수정됨! 카운트다운 시작해!"
        evaluationScheduler.scheduleEvaluation(comboId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_GET_SUCCESS.getCode(),
                        SuccessCode.COMBO_GET_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합 목록 조회 - 이태훈",
            description = """
            특정 사용자의 활성 조합 목록을 조회한다. (삭제되지 않은 조합만)
            즐겨찾기가 있으면 상단에 표시된다.
            각 조합에 포함된 기기 목록도 함께 반환한다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ComboListResponseDto>>> getComboList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ComboListResponseDto> result = comboService.getComboList(customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_LIST_SUCCESS.getCode(),
                        SuccessCode.COMBO_LIST_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합 수정 - 이태훈",
            description = """
            조합명(comboName)을 수정한다.
            본인의 조합만 수정할 수 있다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없음 (본인의 조합이 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PutMapping("/{comboId}")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> updateCombo(
            @PathVariable Long comboId,
            @Valid @RequestBody ComboUpdateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ComboDetailResponseDto result = comboService.updateCombo(comboId, request, customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_UPDATE_SUCCESS.getCode(),
                        SuccessCode.COMBO_UPDATE_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합 삭제 (소프트 삭제) - 이태훈",
            description = """
            조합을 휴지통으로 이동한다. (소프트 삭제)
            30일 후 자동으로 영구 삭제된다.
            본인의 조합만 삭제할 수 있다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없음 (본인의 조합이 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @DeleteMapping("/{comboId}")
    public ResponseEntity<ApiResponse<Void>> deleteCombo(
            @PathVariable Long comboId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        comboService.deleteCombo(comboId, customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_DELETE_SUCCESS.getCode(),
                        SuccessCode.COMBO_DELETE_SUCCESS.getMessage(),
                        null
                )
        );
    }

    // ========== 즐겨찾기 (핀) ==========

    @Operation(
            summary = "조합 즐겨찾기 토글 - 이태훈",
            description = """
            조합의 즐겨찾기 상태를 토글한다.
            - 즐겨찾기가 없으면 설정
            - 즐겨찾기가 있으면 해제
            본인의 조합만 변경할 수 있다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없음 (본인의 조합이 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/{comboId}/pin")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> togglePin(
            @PathVariable Long comboId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ComboDetailResponseDto result = comboService.togglePin(comboId, customUserDetails);
        String code = result.getIsPinned() 
                ? SuccessCode.COMBO_PIN_SUCCESS.getCode() 
                : SuccessCode.COMBO_UNPIN_SUCCESS.getCode();
        String message = result.getIsPinned() 
                ? SuccessCode.COMBO_PIN_SUCCESS.getMessage() 
                : SuccessCode.COMBO_UNPIN_SUCCESS.getMessage();
        
        return ResponseEntity.ok(
                ApiResponse.success(code, message, result)
        );
    }

    // ========== 기기 추가/삭제 ==========

    @Operation(
            summary = "조합에 기기 추가 - 이태훈, 박은서",
            description = """
            조합에 기기를 추가한다.
            이미 추가된 기기는 중복 추가할 수 없다.
            총 가격이 자동으로 업데이트된다.
            본인의 조합에만 기기를 추가할 수 있다. 
            + 3초 디바운싱 기능 有 (은서 구현)
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없음 (본인의 조합이 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조합 또는 기기를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "이미 조합에 추가된 기기",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/{comboId}/devices")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> addDeviceToCombo(
            @PathVariable Long comboId,
            @Valid @RequestBody ComboDeviceAddRequestDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ComboDetailResponseDto result = comboService.addDeviceToCombo(comboId, request, customUserDetails);

        // 2.  [추가] "방금 수정됨! 카운트다운 시작해!"
        evaluationScheduler.scheduleEvaluation(comboId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_DEVICE_ADD_SUCCESS.getCode(),
                        SuccessCode.COMBO_DEVICE_ADD_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합에서 기기 삭제 - 이태훈, 박은서",
            description = """
            조합에서 기기를 삭제한다.
            총 가격이 자동으로 업데이트된다.
            본인의 조합에서만 기기를 삭제할 수 있다.
            + 3초 디바운싱 기능 추가 (은서 구현)
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없음 (본인의 조합이 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "조합에 해당 기기가 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @SecurityRequirement(name = "JWT TOKEN")
    @DeleteMapping("/{comboId}/devices/{deviceId}")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> removeDeviceFromCombo(
            @PathVariable Long comboId,
            @PathVariable Long deviceId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ComboDetailResponseDto result = comboService.removeDeviceFromCombo(comboId, deviceId, customUserDetails);

        // 2. [추가] "방금 수정됨! 카운트다운 시작해!"
        evaluationScheduler.scheduleEvaluation(comboId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_DEVICE_REMOVE_SUCCESS.getCode(),
                        SuccessCode.COMBO_DEVICE_REMOVE_SUCCESS.getMessage(),
                        result
                )
        );
    }

    // ========== 휴지통 ==========

    @Operation(
            summary = "휴지통 목록 조회 - 이태훈",
            description = """
            사용자의 휴지통에 있는 조합 목록을 조회한다.
            각 조합의 영구 삭제까지 남은 일수를 함께 반환한다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/trash")
    public ResponseEntity<ApiResponse<List<ComboTrashResponseDto>>> getTrashList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ComboTrashResponseDto> result = comboService.getTrashList(customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_TRASH_LIST_SUCCESS.getCode(),
                        SuccessCode.COMBO_TRASH_LIST_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "휴지통에서 복구 - 이태훈",
            description = """
            휴지통에 있는 조합을 복구한다.
            본인의 조합만 복구할 수 있다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없음 (본인의 조합이 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/{comboId}/restore")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> restoreCombo(
            @PathVariable Long comboId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ComboDetailResponseDto result = comboService.restoreCombo(comboId, customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_RESTORE_SUCCESS.getCode(),
                        SuccessCode.COMBO_RESTORE_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "휴지통에서 영구 삭제 - 이태훈",
            description = """
            휴지통에 있는 조합을 영구 삭제한다.
            여러 조합을 한 번에 삭제할 수 있다.
            본인의 조합만 영구 삭제할 수 있다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없음 (본인의 조합이 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "휴지통에 없는 조합",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @DeleteMapping("/trash")
    public ResponseEntity<ApiResponse<Void>> permanentDeleteCombos(
            @Valid @RequestBody ComboPermanentDeleteRequestDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        comboService.permanentDeleteCombos(request.getComboIds(), customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_PERMANENT_DELETE_SUCCESS.getCode(),
                        SuccessCode.COMBO_PERMANENT_DELETE_SUCCESS.getMessage(),
                        null
                )
        );
    }

    // ========== 평가 점수 조회 (Polling 용) ==========

    @Operation(
            summary = "조합 평가 점수 조회 - 박은서",
            description = """
            특정 조합의 최신 평가 점수를 조회한다.
            워커가 계산을 마친 후 DB에 저장된 값을 가져온다.
            프론트엔드에서는 1초 간격으로 폴링(Polling)하여 결과를 확인해야 한다.
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "평가 데이터가 없음 (아직 계산 중이거나 조합이 없음)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{comboId}/evaluation")
    public ResponseEntity<ApiResponse<ComboEvaluationResponseDto>> getComboEvaluation(
            @PathVariable Long comboId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ComboEvaluationResponseDto result = comboService.getComboEvaluation(comboId, customUserDetails);

        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_GET_SUCCESS.getCode(),
                        "평가 점수 조회 성공",
                        result
                )
        );
    }
}