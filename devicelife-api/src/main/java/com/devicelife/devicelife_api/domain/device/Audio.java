package com.devicelife.devicelife_api.domain.device;

import com.devicelife.devicelife_api.domain.device.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

/**
 * 🎧 오디오 (Audio - 이어폰/헤드폰)
 * 연결은 쉽지만 코덱 등에 따른 성능(음질) 최적화가 중요한 기기입니다.
 * 서브 디바이스로서 호스트 기기의 코덱 지원에 따라 음질이 달라집니다.
 */
@Entity
@Table(name = "audioDevices")
@DiscriminatorValue("AUDIO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Audio extends Device {

    /**
     * 폼팩터
     * 커널형 | 오픈형 | 온이어 | 오버이어
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "formFactor", nullable = false, length = 20)
    private AudioFormFactor formFactor;

    /**
     * 지원 코덱 목록
     * SBC, AAC, LDAC, aptX, SSC 등
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "supportedCodecs", columnDefinition = "json")
    private List<String> supportedCodecs;

    /**
     * 연결 방식
     * 무선(TWS) | 유선(3.5mm) | 유선(USB-C)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "connectionType", nullable = false, length = 20)
    private AudioConnectionType connectionType;

    /**
     * 노이즈 캔슬링 (ANC) 지원 여부
     */
    @Column(name = "hasAnc")
    private Boolean hasAnc;

    /**
     * 케이스 충전 방식
     * USB-C | Lightning | 무선 충전 지원 여부
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "caseChargingType", length = 30)
    private CaseChargingType caseChargingType;

    /**
     * 드라이버 크기 (mm)
     */
    @Column(name = "driverSizeMm")
    private Double driverSizeMm;

    /**
     * 배터리 수명 - 본체 (시간)
     */
    @Column(name = "batteryLifeHours")
    private Integer batteryLifeHours;

    /**
     * 배터리 수명 - 케이스 포함 총 (시간)
     */
    @Column(name = "totalBatteryLifeHours")
    private Integer totalBatteryLifeHours;

    /**
     * 무게 (g) - 본체 한쪽 기준
     */
    @Column(name = "weightGram")
    private Integer weightGram;

    /**
     * 멀티포인트 지원 여부 (동시 다중 연결)
     */
    @Column(name = "hasMultipoint")
    private Boolean hasMultipoint;

    /**
     * 특정 코덱 지원 여부 확인
     */
    public boolean supportsCodec(AudioCodec codec) {
        if (supportedCodecs == null || supportedCodecs.isEmpty()) {
            return false;
        }
        return supportedCodecs.stream()
            .anyMatch(c -> c.equalsIgnoreCase(codec.name()) 
                || c.equalsIgnoreCase(codec.getDisplayName()));
    }

    /**
     * 하이레졸루션 코덱 지원 여부
     */
    public boolean supportsHiRes() {
        if (supportedCodecs == null) return false;
        return supportedCodecs.stream()
            .anyMatch(c -> c.equalsIgnoreCase("LDAC") 
                || c.equalsIgnoreCase("APTX_HD")
                || c.equalsIgnoreCase("LHDC"));
    }

    /**
     * Apple 기기 최적화 여부 (AAC 코덱)
     */
    public boolean isAppleOptimized() {
        return supportsCodec(AudioCodec.AAC);
    }

    /**
     * 무선 여부
     */
    public boolean isWireless() {
        return connectionType != null && connectionType.isWireless();
    }
}
