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
 * ⌨️ 키보드 (Keyboard)
 * 연결하려는 OS(Win/Mac)에 따라 키 배열 호환성이 달라지는 기기입니다.
 * 서브 디바이스로서 호스트 기기의 OS에 따라 레이아웃 최적화가 필요합니다.
 */
@Entity
@Table(name = "keyboards")
@DiscriminatorValue("KEYBOARD")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Keyboard extends Device {

    /**
     * 레이아웃 지원 목록
     * Windows, macOS, iOS, Android 지원 여부
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "supportedLayouts", columnDefinition = "json")
    private List<String> supportedLayouts;

    /**
     * 연결 방식
     * 블루투스 | 전용 동글(2.4GHz) | 유선
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "connectionType", nullable = false, length = 30)
    private PeripheralConnectionType connectionType;

    /**
     * 배열 크기
     * 풀배열(104키) | 텐키리스(TKL) | 미니(60%)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "keyboardSize", length = 20)
    private KeyboardSize keyboardSize;

    /**
     * 스위치 (축) 종류
     * 청축 | 적축/갈축 | 펜타그래프 | 무접점
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "switchType", length = 20)
    private KeyboardSwitchType switchType;

    /**
     * 멀티 페어링 - 동시 연결 가능 기기 수
     */
    @Column(name = "multiPairingCount")
    private Integer multiPairingCount;

    /**
     * 백라이트 지원 여부
     */
    @Column(name = "hasBacklight")
    private Boolean hasBacklight;

    /**
     * RGB 지원 여부
     */
    @Column(name = "hasRgb")
    private Boolean hasRgb;

    /**
     * 핫스왑 지원 여부 (스위치 교체 가능)
     */
    @Column(name = "hasHotswap")
    private Boolean hasHotswap;

    /**
     * 배터리 용량 (mAh)
     */
    @Column(name = "batteryMah")
    private Integer batteryMah;

    /**
     * 무게 (g)
     */
    @Column(name = "weightGram")
    private Integer weightGram;

    /**
     * 특정 레이아웃 지원 여부 확인
     */
    public boolean supportsLayout(String layout) {
        if (supportedLayouts == null || supportedLayouts.isEmpty()) {
            return false;
        }
        return supportedLayouts.stream()
            .anyMatch(l -> l.equalsIgnoreCase(layout));
    }

    /**
     * macOS 레이아웃 지원 여부
     */
    public boolean supportsMacLayout() {
        return supportsLayout("macOS") || supportsLayout("Mac");
    }

    /**
     * Windows 레이아웃 지원 여부
     */
    public boolean supportsWindowsLayout() {
        return supportsLayout("Windows") || supportsLayout("Win");
    }

    /**
     * 블루투스 연결 지원 여부
     */
    public boolean supportsBluetooth() {
        return connectionType != null && connectionType.supportsBluetooth();
    }

    /**
     * 기계식 키보드 여부
     */
    public boolean isMechanical() {
        return switchType != null && switchType.isMechanical();
    }
}
