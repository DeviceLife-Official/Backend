package com.devicelife.devicelife_api.domain.combo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ComboDeviceResponseDto {
    private Long deviceId;
    private String name;           // 제품명
    private String modelCode;      // 모델 코드
    private String brandName;      // 브랜드명
    private String deviceType;     // 디바이스 타입 (SMARTPHONE, LAPTOP 등)
    private Integer price;         // 가격 (원래 통화)
    private String priceCurrency;  // 가격 통화 (KRW, USD, EUR 등)
    private Integer priceKrw;      // 가격 (KRW로 환전된 값)
    private String imageUrl;       // 이미지 URL
    private LocalDateTime addedAt; // 추가 일시
}
