package com.devicelife.devicelife_api.domain.device;

import com.devicelife.devicelife_api.domain.device.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 📱 스마트폰 (Smartphone)
 * 생태계 호환성의 절대적인 기준점입니다.
 * 메인 디바이스로서 다른 기기들이 연결되는 허브 역할을 합니다.
 */
@Entity
@Table(name = "smartphones")
@DiscriminatorValue("SMARTPHONE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Smartphone extends Device {

    /**
     * 운영체제 (핵심 기준)
     * iOS | Android
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "os", nullable = false, length = 20)
    private OperatingSystem os;

    /**
     * 충전 단자
     * USB-C | Lightning | Micro-USB
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "chargingPort", nullable = false, length = 20)
    private ChargingPort chargingPort;

    /**
     * 무선 충전 방식
     * MagSafe | Qi | None
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "wirelessCharging", length = 20)
    private WirelessCharging wirelessCharging;

    /**
     * 최대 입력 전력 (W)
     * 유선 충전 시 최대 입력값 (e.g., 25W, 45W)
     */
    @Column(name = "maxInputPowerW")
    private Integer maxInputPowerW;

    /**
     * 생체 인식 방식
     * FaceID | Fingerprint | On-Screen
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "biometricType", length = 30)
    private BiometricType biometricType;

    /**
     * 저장 용량 (GB)
     * 128GB ~ 1TB
     */
    @Column(name = "storageGb")
    private Integer storageGb;

    /**
     * RAM 용량 (GB)
     */
    @Column(name = "ramGb")
    private Integer ramGb;

    /**
     * 화면 크기 (인치)
     */
    @Column(name = "screenInch")
    private Double screenInch;

    /**
     * 배터리 용량 (mAh)
     */
    @Column(name = "batteryMah")
    private Integer batteryMah;

    /**
     * Apple 생태계 호환 여부
     */
    public boolean isAppleEcosystem() {
        return os != null && os.isAppleEcosystem();
    }

    /**
     * Android 생태계 호환 여부
     */
    public boolean isAndroidEcosystem() {
        return os != null && os.isAndroidEcosystem();
    }
}
