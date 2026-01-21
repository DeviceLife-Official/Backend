package com.devicelife.devicelife_api.controller.auth;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.devicelife.devicelife_api.common.response.SuccessCode.*;

@Tag(
        name = "Auth",
        description = """
        보안 관련 API - by 남성현
        - 일반로그인
        - 회원가입
        """
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 ",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4001",
                    description = "이미 중복되는 email (중복금지)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/join")
    @Operation(summary = "회원가입 API", description = "아이디(이메일), 비밀번호, 닉네임, 전화번호 입력")
    public ApiResponse<AuthDto.joinResDto> join(@RequestBody @Valid AuthDto.joinReqDto dto) {
        return ApiResponse.success(
                USER_2001.getCode(),
                USER_2001.getMessage(),
                authService.join(dto));
    }

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 ",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4041",
                    description = "해당 email을 사용하는 사용자가 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4006",
                    description = "비밀번호 불일치",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "아이디(이메일), 비밀번호 입력")
    public ApiResponse<AuthDto.loginResDto> login(@RequestBody @Valid AuthDto.loginReqDto dto) {
        return ApiResponse.success(
                USER_2002.getCode(),
                USER_2002.getMessage(),
                authService.login(dto)
        );
    }

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 ",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4012",
                    description = "유효하지 않은 토큰",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4013",
                    description = "이미 로그아웃 처리된 토큰이거나 존재하지 않는 토큰",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @SecurityRequirement(name = "JWT TOKEN")
    @PostMapping("/refresh")
    @Operation(summary = "accessToken 재발급 API", description = "리프레시 토큰을 통해 엑세스 토큰 재발급")
    public ApiResponse<AuthDto.refreshResDto> refresh(@RequestHeader("refreshToken") String refreshToken) {
        return ApiResponse.success(
                USER_2004.getCode(),
                USER_2004.getMessage(),
                authService.refresh(refreshToken));
    }

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 ",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @SecurityRequirement(name = "JWT TOKEN")
    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃 API 호출 시 DB 내 리프레시 토큰 정보 삭제")
    public ApiResponse<Void> logout(@RequestHeader("refreshToken") String refreshToken) {

        authService.logout(refreshToken);

        return ApiResponse.success(
                COMMON_2001.getCode(),
                COMMON_2001.getMessage(),
                null);
    }

/*
    @GetMapping("find-id")
    @Operation(summary = "아이디 찾기 API", description = "이름, 전화번호 입력")
    public ApiResponse<AuthDto.findIdResDto> findId(@RequestBody @Valid AuthDto.findIdReqDto dto) {
        return ApiResponse.success(
                USER_2003.getCode(),
                USER_2003.getMessage(),
                authService.login(dto)
        );
    }*/
}
