package com.devicelife.devicelife_api.controller.combo;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboCreateRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboDeviceAddRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboPermanentDeleteRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboUpdateRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.response.*;
import com.devicelife.devicelife_api.service.combo.ComboService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Combo",
        description = """
        기기 조합 관리 API
        - 조합 생성/수정/조회/삭제
        - 조합에 기기 추가/삭제
        - 즐겨찾기(핀) 관리
        - 휴지통 관리 (목록/복구/영구삭제)
        """
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/combos")
public class ComboController {

    private final ComboService comboService;

    // ========== 조합 기본 CRUD ==========

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

    @Operation(
            summary = "조합 상세 조회",
            description = """
            특정 조합의 상세 정보를 조회한다.
            포함된 기기 목록도 함께 반환한다.
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
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{comboId}")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> getComboDetail(
            @PathVariable Long comboId) {
        ComboDetailResponseDto result = comboService.getComboDetail(comboId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_GET_SUCCESS.getCode(),
                        SuccessCode.COMBO_GET_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합 목록 조회",
            description = """
            특정 사용자의 활성 조합 목록을 조회한다. (삭제되지 않은 조합만)
            즐겨찾기가 있으면 상단에 표시된다.
            
            [JWT 전환 예정]
            - 현재: userId를 쿼리 파라미터로 받음
            - 추후: JWT 인증 도입 시 userId는 토큰에서 추출하도록 변경
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
            @RequestParam Long userId) {
        List<ComboListResponseDto> result = comboService.getComboList(userId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_LIST_SUCCESS.getCode(),
                        SuccessCode.COMBO_LIST_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합명 수정",
            description = """
            조합명(comboName)을 수정한다.
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
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PutMapping("/{comboId}")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> updateCombo(
            @PathVariable Long comboId,
            @Valid @RequestBody ComboUpdateRequestDto request) {
        ComboDetailResponseDto result = comboService.updateCombo(comboId, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_UPDATE_SUCCESS.getCode(),
                        SuccessCode.COMBO_UPDATE_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합 삭제 (소프트 삭제)",
            description = """
            조합을 휴지통으로 이동한다. (소프트 삭제)
            30일 후 자동으로 영구 삭제된다.
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
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @DeleteMapping("/{comboId}")
    public ResponseEntity<ApiResponse<Void>> deleteCombo(
            @PathVariable Long comboId) {
        comboService.deleteCombo(comboId);
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
            summary = "조합 즐겨찾기 토글",
            description = """
            조합의 즐겨찾기 상태를 토글한다.
            - 즐겨찾기가 없으면 설정
            - 즐겨찾기가 있으면 해제
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
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/{comboId}/pin")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> togglePin(
            @PathVariable Long comboId) {
        ComboDetailResponseDto result = comboService.togglePin(comboId);
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
            summary = "조합에 기기 추가",
            description = """
            조합에 기기를 추가한다.
            이미 추가된 기기는 중복 추가할 수 없다.
            총 가격이 자동으로 업데이트된다.
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
            @Valid @RequestBody ComboDeviceAddRequestDto request) {
        ComboDetailResponseDto result = comboService.addDeviceToCombo(comboId, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_DEVICE_ADD_SUCCESS.getCode(),
                        SuccessCode.COMBO_DEVICE_ADD_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "조합에서 기기 삭제",
            description = """
            조합에서 기기를 삭제한다.
            총 가격이 자동으로 업데이트된다.
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
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "조합에 해당 기기가 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @DeleteMapping("/{comboId}/devices/{deviceId}")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> removeDeviceFromCombo(
            @PathVariable Long comboId,
            @PathVariable Long deviceId) {
        ComboDetailResponseDto result = comboService.removeDeviceFromCombo(comboId, deviceId);
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
            summary = "휴지통 목록 조회",
            description = """
            사용자의 휴지통에 있는 조합 목록을 조회한다.
            각 조합의 영구 삭제까지 남은 일수를 함께 반환한다.
            
            [JWT 전환 예정]
            - 현재: userId를 쿼리 파라미터로 받음
            - 추후: JWT 인증 도입 시 userId는 토큰에서 추출하도록 변경
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
            @RequestParam Long userId) {
        List<ComboTrashResponseDto> result = comboService.getTrashList(userId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_TRASH_LIST_SUCCESS.getCode(),
                        SuccessCode.COMBO_TRASH_LIST_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "휴지통에서 복구",
            description = """
            휴지통에 있는 조합을 복구한다.
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
                    description = "조합을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/{comboId}/restore")
    public ResponseEntity<ApiResponse<ComboDetailResponseDto>> restoreCombo(
            @PathVariable Long comboId) {
        ComboDetailResponseDto result = comboService.restoreCombo(comboId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_RESTORE_SUCCESS.getCode(),
                        SuccessCode.COMBO_RESTORE_SUCCESS.getMessage(),
                        result
                )
        );
    }

    @Operation(
            summary = "휴지통에서 영구 삭제",
            description = """
            휴지통에 있는 조합을 영구 삭제한다.
            여러 조합을 한 번에 삭제할 수 있다.
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
            @Valid @RequestBody ComboPermanentDeleteRequestDto request) {
        comboService.permanentDeleteCombos(request.getComboIds());
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.COMBO_PERMANENT_DELETE_SUCCESS.getCode(),
                        SuccessCode.COMBO_PERMANENT_DELETE_SUCCESS.getMessage(),
                        null
                )
        );
    }
}