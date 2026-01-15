package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 충전기 포트 타입
 */
@Getter
@RequiredArgsConstructor
public enum PortType {

    USB_C("USB-C"),
    USB_A("USB-A"),
    LIGHTNING("Lightning"),
    WIRELESS("무선");

    private final String displayName;
}
