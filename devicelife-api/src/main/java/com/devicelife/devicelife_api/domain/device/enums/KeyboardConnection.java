package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 키보드 연결 방식 (태블릿용)
 */
@Getter
@RequiredArgsConstructor
public enum KeyboardConnection {

    SMART_CONNECTOR("Smart Connector (포고핀)"),
    POGO_PIN("포고핀"),
    BLUETOOTH("블루투스"),
    NONE("미지원");

    private final String displayName;
}
