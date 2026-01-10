package com.devicelife.devicelife_api.domain.onboarding.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OnboardingCompleteRequestDto {

    @NotNull
    private Long userId;
}