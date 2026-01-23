package com.devicelife.devicelife_api.domain.user.dto;

import com.devicelife.devicelife_api.domain.user.AuthProvider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class MyPageProfileDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myProfileResDto {
        String username;
        LocalDateTime createdAt;
        String email;
        List<String> lifestyleList;
        AuthProvider authProvider;
    }

    @Getter
    @Setter
    public static class myProfileModifyReqDto {
        String username;
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email;
        List<String> lifestyleList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myProfileModifyResDto {
        String username;
        String email;
        List<String> lifestyleList;
    }

    @Getter
    @Setter
    public static class myPasswordModifyReqDto {
        @NotBlank
        String oldPassword;
        @NotBlank
        @Size(min = 8, max = 20)
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]*$",
                message = "비밀번호는 영문과 숫자를 포함해야 하고 8~20자여야합니다.")
        String newPassword;
        @NotBlank
        String newPasswordConfirm;
    }

}
