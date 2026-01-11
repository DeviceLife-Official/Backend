package com.devicelife.devicelife_api.domain.combo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ComboTrashResponseDto {
    private Long comboId;
    private String comboName;
    private Integer totalPrice;
    private Integer deviceCount;
    private LocalDateTime deletedAt;
    private Long daysUntilPermanentDelete;
}
