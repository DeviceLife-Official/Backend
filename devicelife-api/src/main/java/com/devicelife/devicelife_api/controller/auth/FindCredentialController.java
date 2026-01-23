package com.devicelife.devicelife_api.controller.auth;

import com.devicelife.devicelife_api.common.mail.MailService;
import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.devicelife.devicelife_api.common.response.SuccessCode.MAIL_SEND_SUCCESS;
import static com.devicelife.devicelife_api.common.response.SuccessCode.USER_2003;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class FindCredentialController {

    private final AuthService authService;
    private final MailService mailService;

    @GetMapping("/find-id")
    @Operation(summary = "아이디 찾기 API", description = "이름, 전화번호 입력")
    public ApiResponse<AuthDto.findIdResDto> findId(@RequestBody @Valid AuthDto.findIdReqDto dto) {
        return ApiResponse.success(
                USER_2003.getCode(),
                USER_2003.getMessage(),
                authService.findId(dto)
        );
    }

    @GetMapping("/find-password/send-mail")
    @Operation(summary = "메일 전송 API (비밀번호 찾기 1단계)", description = "이름, 전화번호 입력")
    public ApiResponse<Void> sendMail(
            @RequestBody @Valid AuthDto.findPasswordReqDto dto,
            HttpSession session) {
        mailService.sendCodeMail(dto, session);

        return ApiResponse.success(
                MAIL_SEND_SUCCESS.getCode(),
                MAIL_SEND_SUCCESS.getMessage(),
                null
        );
    }

}
