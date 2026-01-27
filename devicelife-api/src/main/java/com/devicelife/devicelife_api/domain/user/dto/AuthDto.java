package com.devicelife.devicelife_api.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

public class AuthDto {

    @Getter
    @Setter
    public static class joinReqDto {

        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email;

        @NotBlank
        @Size(min = 8, max = 20)
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]*$",
                message = "비밀번호는 영문과 숫자를 포함해야 하고 8~20자여야합니다.")
        String password;

        @NotBlank
        String username;

        @NotBlank
        @Pattern(
                regexp = "^[0-9]+$",
                message = "전화번호는 숫자만 입력해야 합니다.")
        String phoneNumber;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class joinResDto {
        Long userId;
    }

    @Getter
    @Setter
    public static class emailDupCheckReqDto {

        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class emailDupCheckResDto {
        Boolean success;
    }

    @Getter
    @Setter
    public static class loginReqDto {

        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email;

        @NotBlank
        @Size(min = 8, max = 20)
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]*$",
                message = "비밀번호는 영문과 숫자를 포함해야 하고 8~20자여야합니다.")
        String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class loginResDto {
        Long userId;
        String accessToken;
        String refreshToken;
    }

    @Getter
    @Setter
    public static class dupCheckLoginIdReqDto {

        @NotBlank
        @Pattern(
                regexp = "^[a-z0-9]{5,15}$",
                message = "아이디는 영문 소문자와 숫자 조합 5~15자여야 합니다.")
        String loginId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class refreshResDto {
        Long userId;
        String accessToken;
    }

    @Getter
    @Setter
    public static class findIdReqDto {

        @NotBlank
        String username;

        @NotBlank
        @Pattern(
                regexp = "^[0-9]+$",
                message = "전화번호는 숫자만 입력해야 합니다.")
        String phoneNumber;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class findIdResDto {
        String emailInfo;
    }

    @Getter
    @Setter
    public static class sendMailReqDto {

        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email;
    }

    @Getter
    @Setter
    public static class verifyCodeReqDto {
        String code;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class verifyCodeResDto {
        String verifyToken;
    }

    @Getter
    @Setter
    public static class resetPasswordReqDto {
        String verifiedToken;
        @NotBlank
        @Size(min = 8, max = 20)
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]*$",
                message = "비밀번호는 영문과 숫자를 포함해야 하고 8~20자여야합니다.")
        String newPassword;
    }



    @Getter
    @Setter
    public static class userDeleteReqDto {

        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email;
    }

}
