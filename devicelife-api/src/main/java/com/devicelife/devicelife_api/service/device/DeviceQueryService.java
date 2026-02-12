package com.devicelife.devicelife_api.service.device;

import com.devicelife.devicelife_api.common.currency.ExchangeRateService;
import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.common.util.CurrencyConverter;
import com.devicelife.devicelife_api.domain.device.*;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceDetailResponseDto;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceItemDto;
import com.devicelife.devicelife_api.domain.device.dto.response.DeviceSearchResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceSortType;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.repository.device.DeviceSearchCustomRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


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
    private final ExchangeRateService exchangeRateService;
    private final EntityManager em;
    private final CurrencyConverter currencyConverter;

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

        // 2. 환율 정보 가져오기
        Map<String, BigDecimal> exchangeRates = exchangeRateService.getExchangeRateMap();

        // 3. Repository를 통해 데이터 조회 (size + 1개 조회)
        List<DeviceItemDto> devices = deviceSearchRepository.searchDevices(
                convertedKeyword,
                cursorData,
                size,
                sort,
                deviceTypes,
                minPrice,
                maxPrice,
                brandIds,
                exchangeRates
        );

        // 4. hasNext 판단 및 실제 반환할 데이터 추출
        boolean hasNext = devices.size() > size;
        List<DeviceItemDto> actualDevices = hasNext
                ? devices.subList(0, size)
                : devices;

        // 5. nextCursor 생성
        String nextCursor = hasNext
                ? encodeCursor(actualDevices.get(actualDevices.size() - 1), sort)
                : null;

        // 6. 응답 DTO 생성
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

    /**
     * 기기 세부 정보 조회
     * PM 요구사항: 모델명, 가격, 카테고리, 브랜드, 연결 단자, 출시일, 태그
     */
    public DeviceDetailResponseDto getDeviceDetail(Long deviceId) {
        Device device = em.find(Device.class, deviceId);
        if (device == null) {
            throw new CustomException(ErrorCode.DEVICE_4041);
        }

        // 가격 KRW 변환
        Integer priceKrw = currencyConverter.convertToKRW(device.getPrice(), device.getPriceCurrency());

        // 포트 정보 추출 (기기 타입별로 다름)
        DeviceDetailResponseDto.PortInfo portInfo = extractPortInfo(device);

        // 무게 정보 추출 (노트북: kg, 나머지: g)
        String weight = extractWeight(device);

        // 브랜드명 안전하게 추출
        String brandName = "";
        if (device.getBrand() != null && device.getBrand().getBrandName() != null) {
            brandName = device.getBrand().getBrandName();
        }

        // 카테고리 안전하게 추출
        String category = "";
        if (device.getDeviceType() != null && device.getDeviceType().getDisplayName() != null) {
            category = device.getDeviceType().getDisplayName();
        }

        return DeviceDetailResponseDto.builder()
                .deviceId(device.getDeviceId())
                .name(device.getName() != null && !device.getName().isEmpty() ? device.getName() : "")
                .modelCode(device.getModelCode() != null && !device.getModelCode().isEmpty() ? device.getModelCode() : "")
                .brandName(brandName)
                .category(category)
                .price(device.getPrice())
                .priceCurrency(device.getPriceCurrency() != null && !device.getPriceCurrency().isEmpty() ? device.getPriceCurrency() : "")
                .priceKrw(priceKrw)
                .releaseDate(device.getReleaseDate())
                .imageUrl(device.getImageUrl() != null && !device.getImageUrl().isEmpty() ? device.getImageUrl() : "")
                .weight(weight)
                .portInfo(portInfo)
                .tags(Collections.emptyList()) // 태그는 빈 배열로 반환 (PM 요청)
                .build();
    }

    /**
     * 기기 타입별 무게 정보 추출
     * 노트북: kg 단위, 나머지: g 단위
     * Smartphone, Smartwatch는 무게 데이터 없음 → null 반환
     */
    private String extractWeight(Device device) {
        if (device instanceof Laptop laptop) {
            return laptop.getWeightKg() != null
                    ? laptop.getWeightKg().stripTrailingZeros().toPlainString() + " kg" : null;
        } else if (device instanceof Tablet tablet) {
            return tablet.getWeightGram() != null ? tablet.getWeightGram() + " g" : null;
        } else if (device instanceof Audio audio) {
            return audio.getWeightGram() != null ? audio.getWeightGram() + " g" : null;
        } else if (device instanceof Keyboard keyboard) {
            return keyboard.getWeightGram() != null ? keyboard.getWeightGram() + " g" : null;
        } else if (device instanceof Mouse mouse) {
            return mouse.getWeightGram() != null ? mouse.getWeightGram() + " g" : null;
        } else if (device instanceof Charger charger) {
            return charger.getWeightGram() != null ? charger.getWeightGram() + " g" : null;
        }
        // Smartphone, Smartwatch는 무게 필드 없음
        return null;
    }

    /**
     * 기기 타입별 포트 정보 추출
     */
    private DeviceDetailResponseDto.PortInfo extractPortInfo(Device device) {
        DeviceDetailResponseDto.PortInfo.PortInfoBuilder builder = DeviceDetailResponseDto.PortInfo.builder();

        if (device instanceof Smartphone smartphone) {
            builder.chargingPort(smartphone.getChargingPort() != null 
                    ? smartphone.getChargingPort().name() : "");
        } else if (device instanceof Laptop laptop) {
            builder.chargingMethod(laptop.getChargingMethod() != null 
                    ? laptop.getChargingMethod().name() : "");
            builder.hasHdmi(laptop.getHasHdmi());
            builder.hasThunderbolt(laptop.getHasThunderbolt());
            builder.hasUsbA(laptop.getHasUsbA());
            builder.usbCPortCount(laptop.getUsbCPortCount());
        } else if (device instanceof Tablet tablet) {
            builder.chargingPort(tablet.getChargingPort() != null 
                    ? tablet.getChargingPort().name() : "");
        } else if (device instanceof Smartwatch smartwatch) {
            builder.chargingMethod(smartwatch.getChargingMethod() != null 
                    ? smartwatch.getChargingMethod().name() : "");
        } else if (device instanceof Audio audio) {
            builder.connectionType(audio.getConnectionType() != null 
                    ? audio.getConnectionType().name() : "");
            // Audio의 케이스 충전 타입을 chargingPort로 매핑
            builder.chargingPort(audio.getCaseChargingType() != null 
                    ? audio.getCaseChargingType().name() : "");
        } else if (device instanceof Keyboard keyboard) {
            builder.connectionType(keyboard.getConnectionType() != null 
                    ? keyboard.getConnectionType().name() : "");
        } else if (device instanceof Mouse mouse) {
            builder.connectionType(mouse.getConnectionType() != null 
                    ? mouse.getConnectionType().name() : "");
        } else if (device instanceof Charger charger) {
            // Charger의 포트 정보를 간단히 표현
            if (charger.getPortConfiguration() != null && !charger.getPortConfiguration().isEmpty()) {
                builder.outputPortType(String.join(", ", charger.getPortConfiguration()));
            } else {
                builder.outputPortType("");
            }
            builder.outputPortCount(charger.getTotalPortCount());
        }

        return builder.build();
    }
}
