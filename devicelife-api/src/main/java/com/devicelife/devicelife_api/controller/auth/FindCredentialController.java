package com.devicelife.devicelife_api.controller.auth;

import com.devicelife.devicelife_api.common.mail.MailService;
import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.devicelife.devicelife_api.common.response.SuccessCode.*;

@Tag(
        name = "FindCredential",
        description = """
        아이디 비밀번호 찾기 관련 API - by 남성현
        - 아이디 찾기
        - 비밀번호 찾기
        """
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/find-credential")
public class FindCredentialController {

    private final AuthService authService;
    private final MailService mailService;

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4041",
                    description = "존재하지 않는 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "REQ_4002",
                    description = "전화번호는 숫자만 입력해야 합니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/find-id")
    @Operation(summary = "아이디 찾기 API", description = "이름, 전화번호 입력")
    public ApiResponse<AuthDto.findIdResDto> findId(@RequestBody @Valid AuthDto.findIdReqDto dto) {
        return ApiResponse.success(
                USER_2003.getCode(),
                USER_2003.getMessage(),
                authService.findId(dto)
        );
    }

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "MAIL_4191",
                    description = "세션이 만료되었거나 잘못된 접근입니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4005",
                    description = "소셜 로그인으로 신규 가입한 유저는 비밀번호가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "MAIL_4001",
                    description = "인증번호 재전송 횟수를 초과했습니다. 잠시 후 다시 시도해 주세요.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "MAIL_4002",
                    description = "너무 자주 요청했습니다. 잠시 후 다시 시도해 주세요.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "MAIL_5001",
                    description = "메일 전송 실패하였습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
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

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "MAIL_4003",
                    description = "인증번호가 만료되었습니다. 재전송해 주세요.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "MAIL_4004",
                    description = "인증번호 입력 시도 횟수를 초과했습니다. 재전송해 주세요.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "MAIL_4191",
                    description = "세션이 만료되었거나 잘못된 접근입니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
    })
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

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "AUTH_4011",
                    description = "인증 정보가 누락되었습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "AUTH_4191",
                    description = "토큰이 만료되었습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "AUTH_4012",
                    description = "유효하지 않은 토큰입니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "REQ_4002",
                    description = "비밀번호는 영문과 숫자를 포함해야 하고 8~20자여야합니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
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
