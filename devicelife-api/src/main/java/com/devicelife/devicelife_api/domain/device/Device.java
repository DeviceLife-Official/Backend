package com.devicelife.devicelife_api.domain.device;

import com.devicelife.devicelife_api.domain.common.BaseTimeEntity;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Device 엔티티 (부모 클래스)
 * 모든 디바이스의 공통 속성을 정의합니다.
 * JOINED 상속 전략을 사용하여 각 디바이스 타입별 테이블이 생성됩니다.
 */
@Entity
@Table(name = "devices")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "deviceType", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceId")
    private Long deviceId;

    /**
     * 디바이스 타입 (SMARTPHONE, LAPTOP, TABLET 등)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "deviceType", insertable = false, updatable = false, nullable = false)
    private DeviceType deviceType;

    @Column(name = "brandId", nullable = false, insertable = false, updatable = false)
    private Long brandId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    /**
     * 제품명 (사용자에게 표시되는 공식 제품명)
     * e.g., "Galaxy Book 4 Pro", "iPhone 15 Pro"
     */
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    /**
     * 모델명 (정확한 식별을 위한 고유 코드)
     * e.g., "NT960XGK-K71A", "A3101"
     */
    @Column(name = "modelCode", length = 120)
    private String modelCode;

    /**
     * 가격 (현재 판매 최저가 또는 출고가)
     */
    @Column(name = "price")
    private Integer price;

    /**
     * 가격 통화
     * e.g., "KRW", "USD", "EUR", "JPY"
     */
    @Column(name = "priceCurrency", length = 10)
    private String priceCurrency;

    /**
     * 출시일
     */
    @Column(name = "releaseDate")
    private LocalDate releaseDate;

    /**
     * 대표 색상 코드 (HEX)
     * e.g., "#1C1C1E"
     */
    @Column(name = "colorHex", length = 7)
    private String colorHex;

    /**
     * 제품 이미지 URL (누끼 처리된 썸네일)
     */
    @Column(name = "imageUrl", length = 500)
    private String imageUrl;
}

