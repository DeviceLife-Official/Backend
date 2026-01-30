package com.devicelife.devicelife_api.domain.combo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ComboListResponseDto {
    private Long comboId;
    private String comboName;
    private Boolean isPinned;
    private LocalDateTime pinnedAt;
    private Integer totalPrice;
    private BigDecimal currentTotalScore;
    private Integer deviceCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ComboDeviceResponseDto> devices;
}
