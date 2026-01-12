package com.devicelife.devicelife_api.domain.content.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PolicySaveRequestDto {

    @NotBlank(message = "약관 타입은 필수입니다.")
    @Size(max = 30, message = "약관 타입은 30자 이하여야 합니다.")
    private String policyType;

    @NotBlank(message = "버전은 필수입니다.")
    @Size(max = 30, message = "버전은 30자 이하여야 합니다.")
    private String version;

    @NotBlank(message = "본문은 필수입니다.")
    private String body;

    private Boolean isActive;
}
