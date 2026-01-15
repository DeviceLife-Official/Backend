package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 스마트워치 충전 방식
 */
@Getter
@RequiredArgsConstructor
public enum WatchChargingMethod {

    PROPRIETARY_PUCK("전용 독(Puck)"),
    QI_WIRELESS("Qi 무선"),
    MAGNETIC("마그네틱"),
    USB_C("USB-C");

    private final String displayName;
}
