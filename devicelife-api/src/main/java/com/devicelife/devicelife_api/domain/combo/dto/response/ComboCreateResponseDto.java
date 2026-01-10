package com.devicelife.devicelife_api.domain.combo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComboCreateResponseDto {
    private Long comboId;
    private String comboName;
}