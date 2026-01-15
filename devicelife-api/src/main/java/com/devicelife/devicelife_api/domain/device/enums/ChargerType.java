package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 충전기 형태
 */
@Getter
@RequiredArgsConstructor
public enum ChargerType {

    ADAPTER("어댑터 (벽걸이)"),
    DESKTOP_STATION("데스크탑 스테이션"),
    WIRELESS_STAND("무선 스탠드"),
    WIRELESS_PAD("무선 패드"),
    POWER_BANK("보조 배터리"),
    CAR_CHARGER("차량용 충전기");

    private final String displayName;

    public boolean isWireless() {
        return this == WIRELESS_STAND || this == WIRELESS_PAD;
    }

    public boolean isPortable() {
        return this == POWER_BANK || this == CAR_CHARGER;
    }
}
