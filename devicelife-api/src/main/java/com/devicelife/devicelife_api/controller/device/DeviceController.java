package com.devicelife.devicelife_api.controller.device;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceDetailResponseDto;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceSearchResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceSortType;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.service.device.DeviceQueryService;
import com.devicelife.devicelife_api.service.recentview.RecentlyViewedDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 기기 검색 및 조회 API Controller
 */
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController implements DeviceControllerDocs {

        private final DeviceQueryService deviceQueryService;
        private final RecentlyViewedDeviceService recentlyViewedDeviceService;

        @Override
        @GetMapping("/search")
        public ApiResponse<DeviceSearchResponseDto> searchDevices(
                        String keyword,
                        String cursor,
                        Integer size,
                        DeviceSortType sortType,
                        List<DeviceType> deviceTypes,
                        Integer minPrice,
                        Integer maxPrice,
                        List<Long> brandIds
        ) {

                DeviceSearchResponseDto result = deviceQueryService.searchDevices(
                        keyword, cursor, size, sortType, deviceTypes, minPrice, maxPrice, brandIds
                );

                return ApiResponse.success(
                                SuccessCode.DEVICE_SEARCH_SUCCESS.getCode(),
                                SuccessCode.DEVICE_SEARCH_SUCCESS.getMessage(),
                                result);
        }

        @Override
        @GetMapping("/{deviceId}")
        public ApiResponse<DeviceDetailResponseDto> getDeviceDetail(
                        @PathVariable Long deviceId,
                        @AuthenticationPrincipal CustomUserDetails customUserDetails
        ) {
                // 기기 세부 정보 조회
                DeviceDetailResponseDto result = deviceQueryService.getDeviceDetail(deviceId);

                // 로그인한 유저라면 최근 본 기기 테이블에 저장
                if (customUserDetails != null) {
                        recentlyViewedDeviceService.recordDeviceView(customUserDetails.getId(), deviceId);
                }

                return ApiResponse.success(
                                SuccessCode.DEVICE_DETAIL_GET_SUCCESS.getCode(),
                                SuccessCode.DEVICE_DETAIL_GET_SUCCESS.getMessage(),
                                result);
        }
}
