package com.devicelife.devicelife_api.controller.auth;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.devicelife.devicelife_api.common.response.SuccessCode.USER_2001;
import static com.devicelife.devicelife_api.common.response.SuccessCode.USER_2002;

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

    @PostMapping("/join")
    @Operation(summary = "회원가입 API", description = "아이디(이메일), 비밀번호, 닉네임, 전화번호 입력")
    public ApiResponse<AuthDto.joinResDto> join(@RequestBody @Valid AuthDto.joinReqDto dto) {
        return ApiResponse.success(
                USER_2001.getCode(),
                USER_2001.getMessage(),
                authService.join(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "아이디(이메일), 비밀번호 입력")
    public ApiResponse<AuthDto.loginResDto> login(@RequestBody @Valid AuthDto.loginReqDto dto) {
        return ApiResponse.success(
                USER_2002.getCode(),
                USER_2002.getMessage(),
                authService.login(dto)
        );
    }
}
