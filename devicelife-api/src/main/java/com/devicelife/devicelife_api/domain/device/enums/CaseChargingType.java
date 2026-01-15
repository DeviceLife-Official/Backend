package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 오디오 케이스 충전 방식
 */
@Getter
@RequiredArgsConstructor
public enum CaseChargingType {

    USB_C("USB-C"),
    LIGHTNING("Lightning"),
    WIRELESS("무선 충전"),
    USB_C_AND_WIRELESS("USB-C + 무선 충전"),
    LIGHTNING_AND_WIRELESS("Lightning + 무선 충전"),
    NONE("케이스 없음");

    private final String displayName;

    public boolean supportsWireless() {
        return this == WIRELESS || this == USB_C_AND_WIRELESS 
            || this == LIGHTNING_AND_WIRELESS;
    }
}
