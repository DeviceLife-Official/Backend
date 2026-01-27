package com.devicelife.devicelife_api.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class EvaluationDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postEvaluationReqDto {
        Long evaluationVersion;
        BigDecimal compatibilityScore;
        BigDecimal convenienceScore;
        BigDecimal lifestyleScore;
        BigDecimal totalScore;

    }
}
