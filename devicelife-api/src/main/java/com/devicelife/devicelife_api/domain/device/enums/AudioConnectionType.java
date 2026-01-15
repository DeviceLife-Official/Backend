package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 오디오 연결 방식
 */
@Getter
@RequiredArgsConstructor
public enum AudioConnectionType {

    TWS("무선 (TWS)", true),
    BLUETOOTH("블루투스", true),
    WIRED_3_5MM("유선 (3.5mm)", false),
    WIRED_USB_C("유선 (USB-C)", false);

    private final String displayName;
    private final boolean wireless;
}
