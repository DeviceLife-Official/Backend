package com.devicelife.devicelife_api.service.device;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceItemDto;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceSearchResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceSortType;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.repository.device.DeviceSearchCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.devicelife.devicelife_api.common.constants.KeywordMapping.KOREAN_TO_ENGLISH;

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
            String keyword,
            String cursorStr,
            Integer sizeParam,
            DeviceSortType sortType,
            List<DeviceType> deviceTypes,
            Integer minPrice,
            Integer maxPrice,
            List<Long> brandIds) {
        // 1. 요청 검증 및 정규화
        int size = normalizeSize(sizeParam);
        DeviceSortType sort = sortType != null ? sortType : DeviceSortType.LATEST;
        CursorData cursorData = decodeCursor(cursorStr, sort);

        String convertedKeyword = convertKeyword(keyword);

        // 2. Repository를 통해 데이터 조회 (size + 1개 조회)
        List<DeviceItemDto> devices = deviceSearchRepository.searchDevices(
                convertedKeyword,
                cursorData,
                size,
                sort,
                deviceTypes,
                minPrice,
                maxPrice,
                brandIds
        );

        // 3. hasNext 판단 및 실제 반환할 데이터 추출
        boolean hasNext = devices.size() > size;
        List<DeviceItemDto> actualDevices = hasNext
                ? devices.subList(0, size)
                : devices;

        // 4. nextCursor 생성
        String nextCursor = hasNext
                ? encodeCursor(actualDevices.get(actualDevices.size() - 1), sort)
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

    private CursorData decodeCursor(String cursor, DeviceSortType sortType) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }

        try {
            String decoded = new String(Base64.getDecoder().decode(cursor));
            String[] parts = decoded.split("_", 2);

            if (parts.length != 2) {
                throw new CustomException(ErrorCode.DEVICE_4002, "커서 형식이 올바르지 않습니다: " + cursor);
            }

            Long deviceId = Long.parseLong(parts[1]);
            String sortValue = parts[0];

            return new CursorData(sortValue, deviceId);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new CustomException(ErrorCode.DEVICE_4002, "커서 형식이 올바르지 않습니다: " + cursor);
        }
    }

    private String encodeCursor(DeviceItemDto device, DeviceSortType sortType) {
        if (device == null) {
            return null;
        }

        String sortValue = switch (sortType) {
            case LATEST -> device.getReleaseDate() != null
                    ? device.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                    : "0000-01-01";
            case NAME_ASC -> device.getName();
            case PRICE_ASC, PRICE_DESC -> String.valueOf(device.getPrice());
        };

        String raw = sortValue + "_" + device.getDeviceId();
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }

    public record CursorData(String sortValue, Long deviceId) {
    }

    private String convertKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return keyword;
        }

        // 문자열 전체에서 한글 키워드를 직접 찾아서 치환
        String result = keyword;
        for (Map.Entry<String, String> entry : KOREAN_TO_ENGLISH.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        // 공백 제거 (DB에서도 공백 제거 후 비교하므로)
        return result.replaceAll("\\s+", "");
    }
}
