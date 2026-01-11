package com.devicelife.devicelife_api.domain.combo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ComboUpdateRequestDto {

    @NotBlank(message = "조합명은 필수입니다.")
    @Size(max = 80, message = "조합명은 최대 80자까지 입력 가능합니다.")
    private String comboName;
}
