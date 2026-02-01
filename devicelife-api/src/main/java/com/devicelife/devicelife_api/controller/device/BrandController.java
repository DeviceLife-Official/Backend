package com.devicelife.devicelife_api.controller.device;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.domain.device.dto.response.BrandResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.service.device.BrandQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 브랜드 조회 API Controller
 */
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController implements BrandControllerDocs {

    private final BrandQueryService brandQueryService;

    @Override
    @GetMapping
    public ApiResponse<List<BrandResponseDto>> getAllBrands(
            DeviceType deviceType
    ) {
        List<BrandResponseDto> result = brandQueryService.getAllBrands(deviceType);

        return ApiResponse.success(
                SuccessCode.BRAND_LIST_SUCCESS.getCode(),
                SuccessCode.BRAND_LIST_SUCCESS.getMessage(),
                result);
    }
}
