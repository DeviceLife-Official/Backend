package com.devicelife.devicelife_api.domain.device;

import com.devicelife.devicelife_api.domain.device.enums.ChargerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

/**
 * 🔋 충전기 (Charger)
 * 모든 기기에 생명을 불어넣는 전력 공급 기기입니다.
 * 노트북을 충전할 힘(출력)이 있는지, 포트 수는 넉넉한지 평가합니다.
 */
@Entity
@Table(name = "chargers")
@DiscriminatorValue("CHARGER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Charger extends Device {

    /**
     * 총 출력 (Total W)
     * 동시 충전 가능한 최대 전력 (e.g., 65W, 100W, 140W)
     */
    @Column(name = "totalPowerW", nullable = false)
    private Integer totalPowerW;

    /**
     * 단일 포트 최대 출력 (Max W)
     * 포트 하나에서 낼 수 있는 최대 힘 (노트북 충전 기준)
     */
    @Column(name = "maxSinglePortPowerW", nullable = false)
    private Integer maxSinglePortPowerW;

    /**
     * 포트 구성
     * 포트 개수 및 타입 리스트 (e.g., ["C", "C", "A"], ["C", "C", "C", "A"])
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "portConfiguration", columnDefinition = "json")
    private List<String> portConfiguration;

    /**
     * 지원 규격 목록
     * PD 3.0 | PPS (삼성 초고속 충전) | QC 등
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "supportedProtocols", columnDefinition = "json")
    private List<String> supportedProtocols;

    /**
     * 형태
     * 어댑터 | 데스크탑 스테이션 | 무선 스탠드
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "chargerType", length = 30)
    private ChargerType chargerType;

    /**
     * GaN (질화갈륨) 충전기 여부
     * GaN 충전기는 더 작고 효율적입니다
     */
    @Column(name = "isGan")
    private Boolean isGan;

    /**
     * 접이식 플러그 여부
     */
    @Column(name = "hasFoldablePlug")
    private Boolean hasFoldablePlug;

    /**
     * 무게 (g)
     */
    @Column(name = "weightGram")
    private Integer weightGram;

    /**
     * USB-C 포트 개수
     */
    public int getUsbCPortCount() {
        if (portConfiguration == null) return 0;
        return (int) portConfiguration.stream()
            .filter(p -> p.equalsIgnoreCase("C") || p.equalsIgnoreCase("USB-C"))
            .count();
    }

    /**
     * USB-A 포트 개수
     */
    public int getUsbAPortCount() {
        if (portConfiguration == null) return 0;
        return (int) portConfiguration.stream()
            .filter(p -> p.equalsIgnoreCase("A") || p.equalsIgnoreCase("USB-A"))
            .count();
    }

    /**
     * 총 포트 개수
     */
    public int getTotalPortCount() {
        return portConfiguration != null ? portConfiguration.size() : 0;
    }

    /**
     * 특정 전력 요구량의 노트북을 충전할 수 있는지 확인
     * @param requiredPowerW 노트북이 요구하는 최소 전력 (W)
     */
    public boolean canChargeLaptop(int requiredPowerW) {
        return maxSinglePortPowerW >= requiredPowerW;
    }

    /**
     * PD (Power Delivery) 지원 여부
     */
    public boolean supportsPowerDelivery() {
        if (supportedProtocols == null) return false;
        return supportedProtocols.stream()
            .anyMatch(p -> p.toUpperCase().contains("PD"));
    }

    /**
     * PPS (삼성 초고속 충전) 지원 여부
     */
    public boolean supportsPps() {
        if (supportedProtocols == null) return false;
        return supportedProtocols.stream()
            .anyMatch(p -> p.equalsIgnoreCase("PPS"));
    }

    /**
     * 퀵차지 (QC) 지원 여부
     */
    public boolean supportsQuickCharge() {
        if (supportedProtocols == null) return false;
        return supportedProtocols.stream()
            .anyMatch(p -> p.toUpperCase().contains("QC"));
    }

    /**
     * 무선 충전기 여부
     */
    public boolean isWirelessCharger() {
        return chargerType != null && chargerType.isWireless();
    }
}
