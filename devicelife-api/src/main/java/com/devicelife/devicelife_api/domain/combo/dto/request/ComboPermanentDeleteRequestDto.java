package com.devicelife.devicelife_api.domain.combo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class ComboPermanentDeleteRequestDto {

    @NotEmpty(message = "삭제할 조합 ID 목록은 필수입니다.")
    private List<Long> comboIds;
}
