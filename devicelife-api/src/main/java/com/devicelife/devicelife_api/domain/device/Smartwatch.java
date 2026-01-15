package com.devicelife.devicelife_api.domain.device;

import com.devicelife.devicelife_api.domain.device.enums.OperatingSystem;
import com.devicelife.devicelife_api.domain.device.enums.WatchChargingMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

/**
 * ⌚ 스마트워치 (Smartwatch)
 * 스마트폰 OS에 따라 사용 가능 여부가 갈리는 가장 폐쇄적인 기기입니다.
 * 서브 디바이스로서 호스트(스마트폰)에 연결되어야 가치가 생깁니다.
 */
@Entity
@Table(name = "smartwatches")
@DiscriminatorValue("SMARTWATCH")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Smartwatch extends Device {

    /**
     * 운영체제
     * watchOS | WearOS | Tizen | Garmin OS
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "os", nullable = false, length = 20)
    private OperatingSystem os;

    /**
     * 호환 스마트폰 OS 목록
     * 연결 가능한 폰 OS (e.g., ['iOS'], ['Android'], ['iOS', 'Android'])
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "compatiblePhoneOs", columnDefinition = "json")
    private List<String> compatiblePhoneOs;

    /**
     * 충전 방식
     * 전용 독(Puck) | Qi 무선
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "chargingMethod", length = 30)
    private WatchChargingMethod chargingMethod;

    /**
     * 스트랩 사이즈 (mm)
     * 호환 스트랩 규격 (e.g., 20mm, 22mm, 45mm)
     */
    @Column(name = "strapSizeMm")
    private Integer strapSizeMm;

    /**
     * 케이스 사이즈 (mm)
     * 워치 본체 크기 (e.g., 41mm, 45mm, 49mm)
     */
    @Column(name = "caseSizeMm")
    private Integer caseSizeMm;

    /**
     * GPS 내장 여부
     */
    @Column(name = "hasGps")
    private Boolean hasGps;

    /**
     * 셀룰러 지원 여부
     */
    @Column(name = "hasCellular")
    private Boolean hasCellular;

    /**
     * 배터리 수명 (일)
     */
    @Column(name = "batteryLifeDays")
    private Integer batteryLifeDays;

    /**
     * 방수 등급 (ATM)
     * e.g., 5ATM, 10ATM
     */
    @Column(name = "waterResistanceAtm")
    private Integer waterResistanceAtm;

    /**
     * 특정 스마트폰 OS와 호환되는지 확인
     */
    public boolean isCompatibleWith(OperatingSystem phoneOs) {
        if (compatiblePhoneOs == null || compatiblePhoneOs.isEmpty()) {
            return false;
        }
        return compatiblePhoneOs.stream()
            .anyMatch(os -> os.equalsIgnoreCase(phoneOs.name()) 
                || os.equalsIgnoreCase(phoneOs.getDisplayName()));
    }

    /**
     * Apple Watch 여부 (watchOS 기반)
     */
    public boolean isAppleWatch() {
        return os == OperatingSystem.WATCHOS;
    }
}
