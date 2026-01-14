package com.devicelife.devicelife_api.domain.content.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquirySaveRequestDto {

    @Email(message = "이메일 형식이어야 합니다.")
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "주제는 필수입니다.")
    @Size(max = 200, message = "주제는 200자 이하여야 합니다.")
    private String subject;

    @NotBlank(message = "본문은 필수입니다.")
    private String message;
}
