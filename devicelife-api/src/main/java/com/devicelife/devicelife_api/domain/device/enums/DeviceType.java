package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 디바이스 타입 구분
 */
@Getter
@RequiredArgsConstructor
public enum DeviceType {

    // 메인 디바이스
    SMARTPHONE("스마트폰", DeviceCategory.MAIN),
    LAPTOP("노트북", DeviceCategory.MAIN),
    TABLET("태블릿", DeviceCategory.MAIN),

    // 서브 디바이스
    SMARTWATCH("스마트워치", DeviceCategory.SUB),
    AUDIO("오디오", DeviceCategory.SUB),
    KEYBOARD("키보드", DeviceCategory.SUB),
    MOUSE("마우스", DeviceCategory.SUB),

    // 전력 공급
    CHARGER("충전기", DeviceCategory.POWER);

    private final String displayName;
    private final DeviceCategory category;

    public enum DeviceCategory {
        MAIN("메인 디바이스"),
        SUB("서브 디바이스"),
        POWER("전력 공급");

        private final String displayName;

        DeviceCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
