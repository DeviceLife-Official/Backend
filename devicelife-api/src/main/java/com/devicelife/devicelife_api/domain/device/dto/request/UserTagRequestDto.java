package com.devicelife.devicelife_api.domain.device.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class UserTagRequestDto {

    //JWT 토큰 도입 완료하여 주석처리
    //@NotNull
    //private Long userId;

    @NotEmpty
    private List<Long> tagIds;
}
