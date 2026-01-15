package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 오디오 기기 폼팩터
 */
@Getter
@RequiredArgsConstructor
public enum AudioFormFactor {

    IN_EAR("커널형 (인이어)"),
    OPEN_EAR("오픈형"),
    ON_EAR("온이어 헤드폰"),
    OVER_EAR("오버이어 헤드폰");

    private final String displayName;

    public boolean isHeadphone() {
        return this == ON_EAR || this == OVER_EAR;
    }

    public boolean isEarphone() {
        return this == IN_EAR || this == OPEN_EAR;
    }
}
