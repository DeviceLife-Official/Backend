package com.devicelife.devicelife_api.domain.combo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ComboDeviceAddRequestDto {

    @NotNull(message = "기기 ID는 필수입니다.")
    private Long deviceId;
}
