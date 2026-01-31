package com.devicelife.devicelife_api.repository.device;

import com.devicelife.devicelife_api.domain.device.dto.response.DeviceItemDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceSortType;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.service.device.DeviceQueryService.CursorData;

import java.util.List;

/**
 * Device 검색을 위한 커스텀 Repository 인터페이스
 * 커서 기반 페이지네이션과 동적 필터링을 지원합니다.
 */
public interface DeviceSearchCustomRepository {

    /**
     * 기기 검색 (커서 기반 페이지네이션)
     *
     * @param keyword     검색 키워드
     * @param cursorData  페이지네이션 커서 (정렬값 + deviceId), null이면 첫 페이지
     * @param size        페이지 크기 (1~60)
     * @param sortType    정렬 타입
     * @param deviceTypes 검색할 기기 타입들 (null이면 전체)
     * @param minPrice    최소 가격 (null이면 제한 없음)
     * @param maxPrice    최대 가격 (null이면 제한 없음)
     * @param brandIds    검색할 브랜드 ID들 (null이면 전체)
     * @return 기기 목록 (size + 1개까지 조회하여 hasNext 판단용)
     */
    List<DeviceItemDto> searchDevices(
            String keyword,
            CursorData cursorData,
            int size,
            DeviceSortType sortType,
            List<DeviceType> deviceTypes,
            Integer minPrice,
            Integer maxPrice,
            List<Long> brandIds
    );
}
