package com.devicelife.devicelife_api.domain.lifestyle.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SetFeaturedRequest {
    @NotBlank
    private String tagKey;

    @NotNull
    private Long deviceId1;

    @NotNull
    private Long deviceId2;

    @NotNull
    private Long deviceId3;
}