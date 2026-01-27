package com.devicelife.devicelife_api.domain.device;

import com.devicelife.devicelife_api.domain.device.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 📝 태블릿 (Tablet)
 * 모바일과 PC 사이를 잇는 하이브리드 기기입니다.
 * 스타일러스와 전용 키보드 호환성이 중요합니다.
 */
@Entity
@Table(name = "tablets")
@DiscriminatorValue("TABLET")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Tablet extends Device {

    /**
     * 운영체제
     * iPadOS | Android | Windows
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "os", nullable = false, length = 20)
    private OperatingSystem os;

    /**
     * 화면 크기 (인치)
     * 대각선 길이 (e.g., 11.0, 12.9)
     */
    @Column(name = "screenInch", nullable = false)
    private Double screenInch;

    /**
     * 스타일러스 종류
     * Apple Pencil 2 | S-Pen | 미지원
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "stylusType", length = 30)
    private StylusType stylusType;

    /**
     * 키보드 연결 방식
     * 전용 포고핀(Smart Connector) | 블루투스
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "keyboardConnection", length = 30)
    private KeyboardConnection keyboardConnection;

    /**
     * 충전 단자
     * USB-C | Lightning
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "chargingPort", nullable = false, length = 20)
    private ChargingPort chargingPort;

    /**
     * 최대 입력 전력 (W)
     */
    @Column(name = "maxInputPowerW")
    private Integer maxInputPowerW;

    /**
     * 저장 용량 (GB)
     */
    @Column(name = "storageGb")
    private Integer storageGb;

    /**
     * RAM 용량 (GB)
     */
    @Column(name = "ramGb")
    private Integer ramGb;

    /**
     * 배터리 용량 (mAh)
     */
    @Column(name = "batteryMah")
    private Integer batteryMah;

    /**
     * 셀룰러 지원 여부
     */
    @Column(name = "hasCellular")
    private Boolean hasCellular;

    /**
     * 무게 (그램)
     */
    @Column(name = "weightGram")
    private Integer weightGram;

    /**
     * Apple Pencil 호환 여부
     */
    public boolean supportsApplePencil() {
        return stylusType != null && stylusType.isApplePencil();
    }

    /**
     * 전용 키보드 연결 지원 여부 (Smart Connector/포고핀)
     */
    public boolean supportsSmartConnector() {
        return keyboardConnection == KeyboardConnection.SMART_CONNECTOR 
            || keyboardConnection == KeyboardConnection.POGO_PIN;
    }
}
