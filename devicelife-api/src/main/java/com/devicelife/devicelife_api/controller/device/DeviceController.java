package com.devicelife.devicelife_api.controller.device;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceSearchResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.service.device.DeviceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 기기 검색 및 조회 API Controller
 */
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController implements DeviceControllerDocs {

        private final DeviceQueryService deviceQueryService;

        @Override
        @GetMapping("/search")
        public ApiResponse<DeviceSearchResponseDto> searchDevices(
                        String cursor,
                        Integer size,
                        List<DeviceType> deviceTypes,
                        Integer minPrice,
                        Integer maxPrice,
                        List<Long> brandIds
        ) {

                DeviceSearchResponseDto result = deviceQueryService.searchDevices(
                                cursor, size, deviceTypes, minPrice, maxPrice, brandIds);

                return ApiResponse.success(
                                SuccessCode.DEVICE_SEARCH_SUCCESS.getCode(),
                                SuccessCode.DEVICE_SEARCH_SUCCESS.getMessage(),
                                result);
        }
}
