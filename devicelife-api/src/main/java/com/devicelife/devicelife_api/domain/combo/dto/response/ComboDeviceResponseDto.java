package com.devicelife.devicelife_api.domain.combo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ComboDeviceResponseDto {
    private Long deviceId;
    private String modelName;
    private String brandName;
    private String categoryName;
    private Integer price;
    private LocalDateTime addedAt;
}
