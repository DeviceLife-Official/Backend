package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 마우스 전원 공급 방식
 */
@Getter
@RequiredArgsConstructor
public enum MousePowerSource {

    USB_C_RECHARGEABLE("USB-C 충전"),
    MICRO_USB_RECHARGEABLE("Micro-USB 충전"),
    AA_BATTERY("AA 건전지"),
    AAA_BATTERY("AAA 건전지"),
    WIRED("유선 (전원 불필요)");

    private final String displayName;

    public boolean isRechargeable() {
        return this == USB_C_RECHARGEABLE || this == MICRO_USB_RECHARGEABLE;
    }

    public boolean usesBattery() {
        return this == AA_BATTERY || this == AAA_BATTERY;
    }
}
