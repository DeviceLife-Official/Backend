package com.devicelife.devicelife_api.domain.device.dto.response;

import com.devicelife.devicelife_api.domain.device.Brand;
import lombok.Builder;
import lombok.Getter;

/**
 * 브랜드 정보 응답 DTO
 */
@Getter
@Builder
public class BrandResponseDto {

    private Long brandId;
    private String brandName;

    public static BrandResponseDto from(Brand brand) {
        return BrandResponseDto.builder()
                .brandId(brand.getBrandId())
                .brandName(brand.getBrandName())
                .build();
    }
}
