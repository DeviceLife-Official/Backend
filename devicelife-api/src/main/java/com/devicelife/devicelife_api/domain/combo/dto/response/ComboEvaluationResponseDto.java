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
    // --- 항목별 점수 및 등급 ---
    private int connectivity;          // 연동성 점수
    private String connectivityGrade;  // [New] 연동성 등급

    private int convenience;           // 편의성 점수
    private String convenienceGrade;   // [New] 편의성 등급

    private int lifestyle;             // 라이프스타일 점수
    private String lifestyleGrade;     // [New] 라이프스타일 등급
    // -------------------------
    private LocalDateTime evaluatedAt;
}
