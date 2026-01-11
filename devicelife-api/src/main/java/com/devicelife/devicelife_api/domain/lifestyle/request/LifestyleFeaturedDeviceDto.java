package com.devicelife.devicelife_api.domain.lifestyle.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LifestyleFeaturedDeviceDto {
    private int slot;              // 1~3
    private Long deviceId;
    private String imageUrl;       // 대표 이미지(없으면 null)
    private String displayName;    // brand + modelName
    private LocalDate releaseDate; // 없으면 null
    private Integer price;         // offers 최저가 -> 없으면 msrp -> 그래도 없으면 null
    private String currency;       // 기본 "KRW"
}