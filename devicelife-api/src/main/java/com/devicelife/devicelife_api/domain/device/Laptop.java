package com.devicelife.devicelife_api.domain.device;

import com.devicelife.devicelife_api.domain.device.enums.ChargingPort;
import com.devicelife.devicelife_api.domain.device.enums.OperatingSystem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 💻 노트북 (Laptop)
 * 가장 많은 전력을 필요로 하는 업무 생산성의 핵심입니다.
 * 메인 디바이스로서 가장 큰 전력 소비 주체입니다.
 */
@Entity
@Table(name = "laptops")
@DiscriminatorValue("LAPTOP")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Laptop extends Device {

    /**
     * 운영체제
     * macOS | Windows | ChromeOS | Linux
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "os", nullable = false, length = 20)
    private OperatingSystem os;

    /**
     * 프로세서 (CPU)
     * e.g., "M3", "Intel Core i7-13700H", "Ryzen 7 7840U"
     */
    @Column(name = "cpu", length = 100)
    private String cpu;

    /**
     * 그래픽 (GPU)
     * e.g., "내장 그래픽", "RTX 4050", "RTX 4090"
     */
    @Column(name = "gpu", length = 100)
    private String gpu;

    /**
     * 최소 필요 전력 (W)
     * 충전을 위해 필요한 최소 전력 (Critical - 충전기 호환성 판단 기준)
     */
    @Column(name = "minRequiredPowerW", nullable = false)
    private Integer minRequiredPowerW;

    /**
     * 충전 방식
     * USB-C(PD) | 전용 어댑터(DC)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "chargingMethod", nullable = false, length = 20)
    private ChargingPort chargingMethod;

    /**
     * HDMI 포트 유무
     */
    @Column(name = "hasHdmi")
    private Boolean hasHdmi;

    /**
     * Thunderbolt 포트 유무
     */
    @Column(name = "hasThunderbolt")
    private Boolean hasThunderbolt;

    /**
     * USB-A 포트 유무
     */
    @Column(name = "hasUsbA")
    private Boolean hasUsbA;

    /**
     * USB-C 포트 개수
     */
    @Column(name = "usbCPortCount")
    private Integer usbCPortCount;

    /**
     * 무게 (kg)
     * 휴대성 평가용
     */
    @Column(name = "weightKg", precision = 4, scale = 2)
    private BigDecimal weightKg;

    /**
     * RAM 용량 (GB)
     */
    @Column(name = "ramGb")
    private Integer ramGb;

    /**
     * 저장 용량 (GB)
     */
    @Column(name = "storageGb")
    private Integer storageGb;

    /**
     * 화면 크기 (인치)
     */
    @Column(name = "screenInch")
    private Double screenInch;

    /**
     * 배터리 용량 (Wh)
     */
    @Column(name = "batteryWh")
    private Integer batteryWh;

    /**
     * USB-C PD 충전 지원 여부
     */
    public boolean supportsUsbCPdCharging() {
        return chargingMethod == ChargingPort.USB_C;
    }

    /**
     * 외장 그래픽 탑재 여부
     */
    public boolean hasDedicatedGpu() {
        return gpu != null && !gpu.contains("내장") && !gpu.contains("Integrated");
    }
}
