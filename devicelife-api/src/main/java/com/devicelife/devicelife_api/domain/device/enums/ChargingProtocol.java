package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 충전 프로토콜/규격
 */
@Getter
@RequiredArgsConstructor
public enum ChargingProtocol {

    PD_2_0("PD 2.0", "USB Power Delivery 2.0"),
    PD_3_0("PD 3.0", "USB Power Delivery 3.0"),
    PD_3_1("PD 3.1", "USB Power Delivery 3.1 (최대 240W)"),
    PPS("PPS", "Programmable Power Supply (삼성 초고속 충전)"),
    QC_3_0("QC 3.0", "Qualcomm Quick Charge 3.0"),
    QC_4_0("QC 4.0", "Qualcomm Quick Charge 4.0"),
    QC_5_0("QC 5.0", "Qualcomm Quick Charge 5.0"),
    SUPER_VOOC("SuperVOOC", "OPPO 초고속 충전"),
    WARP_CHARGE("Warp Charge", "OnePlus 초고속 충전"),
    APPLE_FAST("Apple Fast Charging", "Apple 고속 충전");

    private final String displayName;
    private final String description;

    /**
     * 노트북 충전 지원 가능 여부 (PD 필수)
     */
    public boolean supportsLaptopCharging() {
        return this == PD_2_0 || this == PD_3_0 || this == PD_3_1;
    }
}
