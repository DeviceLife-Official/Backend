package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 충전 단자 종류
 */
@Getter
@RequiredArgsConstructor
public enum ChargingPort {

    USB_C("USB-C"),
    LIGHTNING("Lightning"),
    MICRO_USB("Micro-USB"),
    DC_ADAPTER("전용 어댑터(DC)");

    private final String displayName;

    /**
     * USB-C 호환 여부
     */
    public boolean isUsbCCompatible() {
        return this == USB_C;
    }
}
