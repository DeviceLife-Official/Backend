package com.devicelife.devicelife_api.domain.device.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 기기 세부 정보 응답 DTO
 * PM 요구사항: 모델명, 가격, 카테고리, 브랜드, 연결 단자, 출시일, 태그
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceDetailResponseDto {

    /**
     * 기기 ID
     */
    private Long deviceId;

    /**
     * 제품명
     */
    private String name;

    /**
     * 모델명
     */
    private String modelCode;

    /**
     * 브랜드명
     */
    private String brandName;

    /**
     * 카테고리 (SMARTPHONE, LAPTOP, TABLET 등)
     */
    private String category;

    /**
     * 가격
     */
    private Integer price;

    /**
     * 통화
     */
    private String priceCurrency;

    /**
     * 가격 (KRW 변환)
     */
    private Integer priceKrw;

    /**
     * 출시일
     */
    private LocalDate releaseDate;

    /**
     * 이미지 URL
     */
    private String imageUrl;

    /**
     * 연결 단자 정보
     * 기기 타입별로 다른 정보 포함
     * - Smartphone/Tablet: chargingPort (USB-C, Lightning 등)
     * - Laptop: chargingMethod, hasHdmi, hasThunderbolt, hasUsbA, usbCPortCount
     * - Smartwatch: chargingMethod
     * - Audio: connectionType, chargingPort
     * - Keyboard/Mouse: connectionType
     */
    private PortInfo portInfo;

    /**
     * 태그 목록 (현재는 빈 배열)
     */
    private List<String> tags;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PortInfo {
        /**
         * 충전 포트 (Smartphone, Tablet, Smartwatch, Audio)
         */
        private String chargingPort;

        /**
         * 충전 방식 (Laptop, Smartwatch)
         */
        private String chargingMethod;

        /**
         * 연결 타입 (Audio, Keyboard, Mouse)
         */
        private String connectionType;

        /**
         * HDMI 포트 유무 (Laptop)
         */
        private Boolean hasHdmi;

        /**
         * Thunderbolt 포트 유무 (Laptop)
         */
        private Boolean hasThunderbolt;

        /**
         * USB-A 포트 유무 (Laptop)
         */
        private Boolean hasUsbA;

        /**
         * USB-C 포트 개수 (Laptop)
         */
        private Integer usbCPortCount;

        /**
         * 출력 포트 타입 (Charger)
         */
        private String outputPortType;

        /**
         * 출력 포트 개수 (Charger)
         */
        private Integer outputPortCount;
    }
}
