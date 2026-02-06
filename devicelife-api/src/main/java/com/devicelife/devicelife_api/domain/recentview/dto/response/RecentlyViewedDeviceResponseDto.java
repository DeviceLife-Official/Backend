package com.devicelife.devicelife_api.domain.recentview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 최근 본 기기 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentlyViewedDeviceResponseDto {

    /**
     * 기기 ID
     */
    private Long deviceId;

    /**
     * 기기명
     */
    private String name;

    /**
     * 모델 코드
     */
    private String modelCode;

    /**
     * 브랜드명
     */
    private String brandName;

    /**
     * 기기 타입 (SMARTPHONE, LAPTOP 등)
     */
    private String deviceType;

    /**
     * 가격
     */
    private Integer price;

    /**
     * 가격 통화
     */
    private String priceCurrency;

    /**
     * 가격 (KRW 변환)
     */
    private Integer priceKrw;

    /**
     * 이미지 URL
     */
    private String imageUrl;

    /**
     * 조회 시간
     */
    private LocalDateTime viewedAt;
}
