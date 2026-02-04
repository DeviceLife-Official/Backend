package com.devicelife.devicelife_api.controller.onboarding;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.onboarding.dto.request.OnboardingCompleteRequestDto;
import com.devicelife.devicelife_api.service.onboarding.OnboardingService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Onboarding",
        description = """
        온보딩 완료 처리 API. by 박은서
        """
)
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT TOKEN")
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    @Operation(
            summary = "온보딩 완료 처리",
            description = """
            users.onboardingCompleted를 true로 설정한다.
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
                    description = "존재하지 않는 userId",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<Void>> complete(
            //@io.swagger.v3.oas.annotations.parameters.RequestBody(
            //        required = true,
            //        description = "userId는 임시(인증 도입 후 토큰에서 추출하도록 변경).",
            //        content = @Content(schema = @Schema(implementation = OnboardingCompleteRequestDto.class))
            //)
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        onboardingService.complete(customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessCode.ONBOARDING_COMPLETE_SUCCESS.getCode(),
                        SuccessCode.ONBOARDING_COMPLETE_SUCCESS.getMessage(),
                        null
                )
        );
    }
}
