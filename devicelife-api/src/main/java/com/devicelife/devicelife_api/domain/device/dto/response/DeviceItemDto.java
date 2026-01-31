package com.devicelife.devicelife_api.domain.device.dto.response;

import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * 개별 기기 정보 DTO
 * 목록 조회 시 반환되는 기기 정보
 * devices 테이블의 공통 정보와 각 기기 타입별 하위 테이블의 구체적인 정보를 포함합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceItemDto {

    /**
     * 기기 ID
     */
    private Long deviceId;

    /**
     * 기기 타입 (SMARTPHONE, LAPTOP 등)
     */
    private DeviceType deviceType;

    /**
     * 브랜드명
     */
    private String brandName;

    /**
     * 제품명
     */
    private String name;

    /**
     * 가격
     */
    private Integer price;

    /**
     * 통화 (KRW, USD 등)
     */
    private String priceCurrency;

    /**
     * 이미지 URL
     */
    private String imageUrl;

    /**
     * 출시일
     */
    private LocalDate releaseDate;

    /**
     * 기기 타입별 구체적인 정보
     * - SMARTPHONE: os, chargingPort, storageGb, ramGb, screenInch, batteryMah 등
     * - LAPTOP: os, cpu, gpu, minRequiredPowerW, ramGb, storageGb 등
     * - 기타 타입별 필드들...
     */
    private Map<String, Object> specifications;

    public static DeviceItemDto of(
            Long deviceId,
            DeviceType deviceType,
            String brandName,
            String name,
            Integer price,
            String priceCurrency,
            String imageUrl,
            LocalDate releaseDate) {
        return DeviceItemDto.builder()
                .deviceId(deviceId)
                .deviceType(deviceType)
                .brandName(brandName)
                .name(name)
                .price(price)
                .priceCurrency(priceCurrency)
                .imageUrl(imageUrl)
                .releaseDate(releaseDate)
                .build();
    }
}
