package com.devicelife.devicelife_api.domain.combo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ComboCreateRequestDto {

    //JWT 도입 완료하여 주석처리
    //@NotNull
    //private Long userId;

    @NotBlank
    @Size(max = 80)
    private String comboName;
}
