package com.devicelife.devicelife_api.domain.device.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 기기 검색 응답 DTO
 * 커서 기반 페이지네이션 정보를 포함한 기기 목록
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceSearchResponseDto {

    /**
     * 기기 목록
     */
    private List<DeviceItemDto> devices;

    /**
     * 다음 페이지를 위한 커서
     * 마지막 페이지인 경우 null
     */
    private String nextCursor;

    /**
     * 다음 페이지 존재 여부
     */
    private boolean hasNext;

    public static DeviceSearchResponseDto of(List<DeviceItemDto> devices, String nextCursor, boolean hasNext) {
        return DeviceSearchResponseDto.builder()
                .devices(devices)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
