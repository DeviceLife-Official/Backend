package com.devicelife.devicelife_api.domain.device.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 기기 세부 정보 응답 DTO
 * PM 요구사항: 모델명, 가격, 카테고리, 브랜드, 무게, 연결 단자, 출시일, 태그
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "기기 세부 정보 응답")
public class DeviceDetailResponseDto {

    @Schema(description = "기기 ID", example = "1")
    private Long deviceId;

    @Schema(description = "제품명", example = "MacBook Air 15 M3")
    private String name;

    @Schema(description = "모델명", example = "MXLQ3KH/A")
    private String modelCode;

    @Schema(description = "브랜드명", example = "Apple")
    private String brandName;

    @Schema(description = "카테고리", example = "노트북")
    private String category;

    @Schema(description = "가격", example = "1899000")
    private Integer price;

    @Schema(description = "통화", example = "KRW")
    private String priceCurrency;

    @Schema(description = "가격 (KRW 변환)", example = "1899000")
    private Integer priceKrw;

    @Schema(description = "출시일", example = "2024-03-08")
    private LocalDate releaseDate;

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "무게 (노트북: kg, 나머지: g). Smartphone·Smartwatch는 데이터 없어 null → 응답에서 제외",
            example = "1.51 kg", nullable = true)
    private String weight;

    @Schema(description = "연결 단자 정보 (기기 타입별로 다른 정보 포함)")
    private PortInfo portInfo;

    @Schema(description = "태그 목록 (현재는 빈 배열)")
    private List<String> tags;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "연결 단자 정보 (기기 타입별 해당 필드만 포함)")
    public static class PortInfo {

        @Schema(description = "충전 포트 (Smartphone, Tablet, Audio)", example = "USB_C")
        private String chargingPort;

        @Schema(description = "충전 방식 (Laptop, Smartwatch)", example = "USB_C")
        private String chargingMethod;

        @Schema(description = "연결 타입 (Audio, Keyboard, Mouse)", example = "BLUETOOTH")
        private String connectionType;

        @Schema(description = "HDMI 포트 유무 (Laptop)", example = "true")
        private Boolean hasHdmi;

        @Schema(description = "Thunderbolt 포트 유무 (Laptop)", example = "true")
        private Boolean hasThunderbolt;

        @Schema(description = "USB-A 포트 유무 (Laptop)", example = "false")
        private Boolean hasUsbA;

        @Schema(description = "USB-C 포트 개수 (Laptop)", example = "2")
        private Integer usbCPortCount;

        @Schema(description = "출력 포트 타입 (Charger)", example = "C, C, A")
        private String outputPortType;

        @Schema(description = "출력 포트 개수 (Charger)", example = "3")
        private Integer outputPortCount;
    }
}
