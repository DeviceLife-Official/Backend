package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 주변기기(키보드/마우스) 연결 방식
 */
@Getter
@RequiredArgsConstructor
public enum PeripheralConnectionType {

    BLUETOOTH("블루투스", true),
    DONGLE_2_4GHZ("전용 동글 (2.4GHz)", true),
    WIRED_USB("유선 (USB)", false),
    BLUETOOTH_AND_DONGLE("블루투스 + 동글", true),
    TRIPLE_MODE("트리플 모드 (유선+블루투스+동글)", true);

    private final String displayName;
    private final boolean wireless;

    public boolean supportsBluetooth() {
        return this == BLUETOOTH || this == BLUETOOTH_AND_DONGLE || this == TRIPLE_MODE;
    }

    public boolean supportsDongle() {
        return this == DONGLE_2_4GHZ || this == BLUETOOTH_AND_DONGLE || this == TRIPLE_MODE;
    }

    public boolean supportsWired() {
        return this == WIRED_USB || this == TRIPLE_MODE;
    }
}
