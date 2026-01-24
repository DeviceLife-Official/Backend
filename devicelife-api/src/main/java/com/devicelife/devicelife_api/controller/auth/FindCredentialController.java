package com.devicelife.devicelife_api.controller.auth;

import com.devicelife.devicelife_api.common.mail.MailService;
import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.devicelife.devicelife_api.common.response.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class FindCredentialController {

    private final AuthService authService;
    private final MailService mailService;

    @PostMapping("/find-id")
    @Operation(summary = "아이디 찾기 API", description = "이름, 전화번호 입력")
    public ApiResponse<AuthDto.findIdResDto> findId(@RequestBody @Valid AuthDto.findIdReqDto dto) {
        return ApiResponse.success(
                USER_2003.getCode(),
                USER_2003.getMessage(),
                authService.findId(dto)
        );
    }

    @PostMapping("/find-password/send-mail")
    @Operation(summary = "메일 전송 API (비밀번호 찾기 1단계)", description = "이메일 입력")
    public ApiResponse<Void> sendMail(
            @RequestBody @Valid AuthDto.sendMailReqDto dto,
            HttpSession session) {
        mailService.sendCodeMail(dto, session);

        return ApiResponse.success(
                MAIL_SEND_SUCCESS.getCode(),
                MAIL_SEND_SUCCESS.getMessage(),
                null
        );
    }

    @PostMapping("/find-password/verify-code")
    @Operation(summary = "코드 입력 API (비밀번호 찾기 2단계)", description = "코드 입력 후 인증 토큰 발급")
    public ApiResponse<AuthDto.verifyCodeResDto> verifyCode(
            @RequestBody @Valid AuthDto.verifyCodeReqDto dto,
            HttpSession session) {
        return ApiResponse.success(
                VERIFY_SUCCESS.getCode(),
                VERIFY_SUCCESS.getMessage(),
                mailService.verifyCodeMail(dto, session)
        );
    }

    @PostMapping("/find-password/reset")
    @Operation(summary = "비밀번호 리셋 API (비밀번호 찾기 3단계)", description = "인증 토큰, 비밀번호")
    public ApiResponse<Void> resetPassword(
            @RequestBody @Valid AuthDto.resetPasswordReqDto dto,
            HttpSession session) {
        mailService.resetPassword(dto, session);
        return ApiResponse.success(
                COMMON_2001.getCode(),
                COMMON_2001.getMessage(),
                null
        );
    }
}
