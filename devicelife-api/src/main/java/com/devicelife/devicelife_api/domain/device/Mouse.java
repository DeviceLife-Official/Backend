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
 * 🖱️ 마우스 (Mouse)
 * 연결 방식(블루투스/동글)과 OS별 제스처 지원 여부가 핵심인 기기입니다.
 * 서브 디바이스로서 호스트 기기의 OS에 따라 제스처/버튼 기능이 달라집니다.
 */
@Entity
@Table(name = "mice")
@DiscriminatorValue("MOUSE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Mouse extends Device {

    /**
     * 연결 방식
     * 블루투스 | 전용 동글 | 유선
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "connectionType", nullable = false, length = 30)
    private PeripheralConnectionType connectionType;

    /**
     * 제스처 지원 OS 목록
     * macOS, Windows, iPadOS 제스처 호환 여부
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gestureSupport", columnDefinition = "json")
    private List<String> gestureSupport;

    /**
     * 충전/배터리 방식
     * USB-C 충전 | 건전지
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "powerSource", length = 30)
    private MousePowerSource powerSource;

    /**
     * 형태
     * 일반형 | 버티컬(인체공학) | 트랙볼
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "mouseType", length = 20)
    private MouseType mouseType;

    /**
     * DPI (최대)
     */
    @Column(name = "maxDpi")
    private Integer maxDpi;

    /**
     * 버튼 개수
     */
    @Column(name = "buttonCount")
    private Integer buttonCount;

    /**
     * 멀티 페어링 - 동시 연결 가능 기기 수
     */
    @Column(name = "multiPairingCount")
    private Integer multiPairingCount;

    /**
     * 무게 (g)
     */
    @Column(name = "weightGram")
    private Integer weightGram;

    /**
     * 조용한 클릭 지원 여부
     */
    @Column(name = "hasSilentClick")
    private Boolean hasSilentClick;

    /**
     * 특정 OS 제스처 지원 여부 확인
     */
    public boolean supportsGestureOn(String os) {
        if (gestureSupport == null || gestureSupport.isEmpty()) {
            return false;
        }
        return gestureSupport.stream()
            .anyMatch(g -> g.equalsIgnoreCase(os));
    }

    /**
     * macOS 제스처 지원 여부
     */
    public boolean supportsMacGestures() {
        return supportsGestureOn("macOS") || supportsGestureOn("Mac");
    }

    /**
     * Windows 제스처 지원 여부
     */
    public boolean supportsWindowsGestures() {
        return supportsGestureOn("Windows") || supportsGestureOn("Win");
    }

    /**
     * 블루투스 연결 지원 여부
     */
    public boolean supportsBluetooth() {
        return connectionType != null && connectionType.supportsBluetooth();
    }

    /**
     * 충전식 여부
     */
    public boolean isRechargeable() {
        return powerSource != null && powerSource.isRechargeable();
    }

    /**
     * 인체공학적 마우스 여부
     */
    public boolean isErgonomic() {
        return mouseType != null && mouseType.isErgonomic();
    }
}
