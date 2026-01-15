package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 무선 충전 방식
 */
@Getter
@RequiredArgsConstructor
public enum WirelessCharging {

    MAGSAFE("MagSafe", true),
    QI("Qi", true),
    NONE("미지원", false);

    private final String displayName;
    private final boolean supported;

    /**
     * Qi 호환 여부 (MagSafe도 Qi 호환)
     */
    public boolean isQiCompatible() {
        return this == MAGSAFE || this == QI;
    }
}
