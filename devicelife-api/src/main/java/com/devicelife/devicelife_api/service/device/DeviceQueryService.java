package com.devicelife.devicelife_api.service.device;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceItemDto;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceSearchResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.repository.device.DeviceSearchCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 기기 조회 서비스
 * 커서 기반 페이지네이션과 필터링을 지원합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceQueryService {

    private final DeviceSearchCustomRepository deviceSearchRepository;

    private static final int DEFAULT_SIZE = 24;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 60;

    public DeviceSearchResponseDto searchDevices(
            String cursorStr,
            Integer sizeParam,
            List<DeviceType> deviceTypes,
            Integer minPrice,
            Integer maxPrice,
            List<Long> brandIds) {
        // 1. 요청 검증 및 정규화
        int size = normalizeSize(sizeParam);
        Long cursor = decodeCursor(cursorStr);

        // 2. Repository를 통해 데이터 조회 (size + 1개 조회)
        List<DeviceItemDto> devices = deviceSearchRepository.searchDevices(
                cursor,
                size,
                deviceTypes,
                minPrice,
                maxPrice,
                brandIds);

        // 3. hasNext 판단 및 실제 반환할 데이터 추출
        boolean hasNext = devices.size() > size;
        List<DeviceItemDto> actualDevices = hasNext
                ? devices.subList(0, size)
                : devices;

        // 4. nextCursor 생성
        String nextCursor = hasNext
                ? encodeCursor(actualDevices.get(actualDevices.size() - 1).getDeviceId())
                : null;

        // 5. 응답 DTO 생성
        return DeviceSearchResponseDto.of(actualDevices, nextCursor, hasNext);
    }

    private int normalizeSize(Integer size) {
        if (size == null) {
            return DEFAULT_SIZE;
        }
        if (size < MIN_SIZE) {
            return MIN_SIZE;
        }
        if (size > MAX_SIZE) {
            return MAX_SIZE;
        }
        return size;
    }

    private Long decodeCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(cursor);
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.DEVICE_4002, "커서 형식이 올바르지 않습니다: " + cursor);
        }
    }

    private String encodeCursor(Long deviceId) {
        return deviceId != null ? deviceId.toString() : null;
    }
}
