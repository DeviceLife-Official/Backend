package com.devicelife.devicelife_api.domain.device.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class UserTagRequestDto {

    @NotNull
    private Long userId;

    @NotEmpty
    private List<Long> tagIds;
}
