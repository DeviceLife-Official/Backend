package com.devicelife.devicelife_api.domain.combo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboEvaluationResponseDto {
    private Long comboId;
    private int totalScore;
    private String grade;       // HIGH, MID, LOW
    private int connectivity;   // scoreA (연동성)
    private int convenience;    // scoreB (편의성/품질)
    private int lifestyle;      // scoreC (라이프스타일)
    private LocalDateTime evaluatedAt;
}
