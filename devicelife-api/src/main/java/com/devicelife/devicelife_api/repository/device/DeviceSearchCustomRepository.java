package com.devicelife.devicelife_api.repository.device;

import com.devicelife.devicelife_api.domain.device.dto.response.DeviceItemDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;

import java.util.List;

/**
 * Device 검색을 위한 커스텀 Repository 인터페이스
 * 커서 기반 페이지네이션과 동적 필터링을 지원합니다.
 */
public interface DeviceSearchCustomRepository {

    /**
     * 기기 검색 (커서 기반 페이지네이션)
     *
     * @param cursor      페이지네이션 커서 (deviceId), null이면 첫 페이지
     * @param size        페이지 크기 (1~60)
     * @param deviceTypes 검색할 기기 타입들 (null이면 전체)
     * @param minPrice    최소 가격 (null이면 제한 없음)
     * @param maxPrice    최대 가격 (null이면 제한 없음)
     * @param brandIds    검색할 브랜드 ID들 (null이면 전체)
     * @return 기기 목록 (size + 1개까지 조회하여 hasNext 판단용)
     */
    List<DeviceItemDto> searchDevices(
            Long cursor,
            int size,
            List<DeviceType> deviceTypes,
            Integer minPrice,
            Integer maxPrice,
            List<Long> brandIds);
}
