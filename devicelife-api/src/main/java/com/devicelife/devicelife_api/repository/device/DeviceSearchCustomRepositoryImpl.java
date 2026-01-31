package com.devicelife.devicelife_api.repository.device;

import com.devicelife.devicelife_api.domain.device.dto.response.DeviceItemDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceSortType;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.service.device.DeviceQueryService.CursorData;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Device 검색 커스텀 Repository 구현체
 * 성능 최적화를 위해 메인 쿼리(기본 정보)와 서브 쿼리(상세 스펙)를 분리하여 실행합니다.
 */
@Repository
@RequiredArgsConstructor
public class DeviceSearchCustomRepositoryImpl implements DeviceSearchCustomRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<DeviceItemDto> searchDevices(
            String keyword,
            CursorData cursorData,
            int size,
            DeviceSortType sortType,
            List<DeviceType> deviceTypes,
            Integer minPrice,
            Integer maxPrice,
            List<Long> brandIds) {
        // 1. 기본 정보 조회 (Paging, Filtering 포함)
        List<DeviceItemDto> devices = findBasicDevices(keyword, cursorData, size, sortType, deviceTypes, minPrice, maxPrice, brandIds);

        if (devices.isEmpty()) {
            return devices;
        }

        // 2. 기기 타입별 ID 그룹핑
        Map<DeviceType, List<Long>> idsByType = devices.stream()
                .collect(Collectors.groupingBy(
                        DeviceItemDto::getDeviceType,
                        Collectors.mapping(DeviceItemDto::getDeviceId, Collectors.toList())));

        // 3. 타입별 상세 스펙 조회 및 병합
        enrichSmartphones(devices, idsByType.get(DeviceType.SMARTPHONE));
        enrichLaptops(devices, idsByType.get(DeviceType.LAPTOP));
        enrichTablets(devices, idsByType.get(DeviceType.TABLET));
        enrichSmartwatches(devices, idsByType.get(DeviceType.SMARTWATCH));
        enrichAudio(devices, idsByType.get(DeviceType.AUDIO));
        enrichKeyboards(devices, idsByType.get(DeviceType.KEYBOARD));
        enrichMice(devices, idsByType.get(DeviceType.MOUSE));
        enrichChargers(devices, idsByType.get(DeviceType.CHARGER));

        return devices;
    }

    /**
     * 1단계: 기본 기기 정보 조회 (Main Query)
     * devices 테이블과 brands 테이블만 조인하여 가볍게 조회
     * 동적 쿼리 빌드로 인덱스 활용 최적화
     */
    private List<DeviceItemDto> findBasicDevices(
            String keyword,
            CursorData cursorData,
            int size,
            DeviceSortType sortType,
            List<DeviceType> deviceTypes,
            Integer minPrice,
            Integer maxPrice,
            List<Long> brandIds) {

        StringBuilder sql = new StringBuilder("""
                SELECT
                    d.deviceId,
                    d.deviceType,
                    d.name,
                    d.price,
                    d.priceCurrency,
                    d.imageUrl,
                    d.releaseDate,
                    b.brandName
                FROM devices d
                JOIN brands b ON d.brandId = b.brandId
                WHERE 1=1
                """);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", size + 1);

        // 동적 조건 추가 (파라미터가 있을 때만 조건 추가하여 인덱스 활용)
        // 공백 제거 후 비교하여 "갤럭시s25" -> "Galaxy S25" 매칭 가능
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND LOWER(REPLACE(d.name, ' ', '')) LIKE LOWER(:keyword)");
            params.addValue("keyword", "%" + keyword.replaceAll("\\s+", "") + "%");
        }

        // 복합 커서 조건 추가
        if (cursorData != null) {
            appendCursorCondition(sql, params, cursorData, sortType);
        }

        if (deviceTypes != null && !deviceTypes.isEmpty()) {
            sql.append(" AND d.deviceType IN (:deviceTypes)");
            List<String> typeNames = deviceTypes.stream().map(Enum::name).toList();
            params.addValue("deviceTypes", typeNames);
        }

        if (brandIds != null && !brandIds.isEmpty()) {
            sql.append(" AND d.brandId IN (:brandIds)");
            params.addValue("brandIds", brandIds);
        }

        if (minPrice != null) {
            sql.append(" AND d.price >= :minPrice");
            params.addValue("minPrice", minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND d.price <= :maxPrice");
            params.addValue("maxPrice", maxPrice);
        }

        // 기기 타입별 데이터 필터링
        sql.append(
                """
                          AND (
                            (d.deviceType = 'SMARTPHONE' AND d.releaseDate >= '2021-01-01' AND (
                                (b.brandName = 'Samsung' AND (d.name LIKE '%Galaxy A%' OR d.name LIKE '%Galaxy S%' OR d.name LIKE '%Galaxy Z%'))
                                OR (b.brandName = 'Apple')
                            ))
                            OR (d.deviceType = 'LAPTOP' AND d.releaseDate >= '2023-01-01')
                            OR (d.deviceType = 'TABLET' AND d.releaseDate >= '2023-01-01' AND b.brandName != 'Huawei')
                            OR (d.deviceType = 'SMARTWATCH' AND d.releaseDate >= '2023-01-01' AND b.brandName IN ('Apple', 'Samsung'))
                            OR (d.deviceType = 'AUDIO' AND d.releaseDate >= '2023-01-01')
                            OR (d.deviceType = 'KEYBOARD' AND d.releaseDate >= '2023-01-01')
                            OR (d.deviceType = 'MOUSE' AND d.releaseDate >= '2023-01-01')
                            OR (d.deviceType = 'CHARGER' AND d.releaseDate >= '2023-01-01')
                          )
                        """);

        // 동적 정렬 조건 추가
        sql.append(" ORDER BY ").append(sortType.getOrderByClause());
        sql.append(" LIMIT :limit");

        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> {
            Long deviceId = rs.getLong("deviceId");
            String deviceTypeStr = rs.getString("deviceType");
            DeviceType deviceType = DeviceType.valueOf(deviceTypeStr);
            String brandName = rs.getString("brandName");
            String name = rs.getString("name");
            Integer price = rs.getInt("price");
            String priceCurrency = rs.getString("priceCurrency");
            String imageUrl = rs.getString("imageUrl");
            java.sql.Date releaseDateSql = rs.getDate("releaseDate");
            java.time.LocalDate releaseDate = releaseDateSql != null ? releaseDateSql.toLocalDate() : null;

            return DeviceItemDto.of(
                    deviceId,
                    deviceType,
                    brandName,
                    name,
                    price,
                    priceCurrency,
                    imageUrl,
                    releaseDate); // specifications는 나중에 채움
        });
    }

    /**
     * 복합 커서 조건을 쿼리에 추가
     * 정렬 타입에 따라 다른 커서 조건을 적용
     */
    private void appendCursorCondition(StringBuilder sql, MapSqlParameterSource params,
                                       CursorData cursorData, DeviceSortType sortType) {
        params.addValue("cursorDeviceId", cursorData.deviceId());

        switch (sortType) {
            case LATEST -> {
                // releaseDate DESC, deviceId DESC
                // (releaseDate < cursorDate) OR (releaseDate = cursorDate AND deviceId < cursorDeviceId)
                LocalDate cursorDate = LocalDate.parse(cursorData.sortValue());
                params.addValue("cursorDate", cursorDate);
                sql.append("""
                         AND (
                            d.releaseDate < :cursorDate
                            OR (d.releaseDate = :cursorDate AND d.deviceId < :cursorDeviceId)
                         )
                        """);
            }
            case NAME_ASC -> {
                // name ASC, deviceId ASC
                // (name > cursorName) OR (name = cursorName AND deviceId > cursorDeviceId)
                params.addValue("cursorName", cursorData.sortValue());
                sql.append("""
                         AND (
                            d.name > :cursorName
                            OR (d.name = :cursorName AND d.deviceId > :cursorDeviceId)
                         )
                        """);
            }
            case PRICE_ASC -> {
                // price ASC, deviceId ASC
                // (price > cursorPrice) OR (price = cursorPrice AND deviceId > cursorDeviceId)
                Integer cursorPrice = Integer.parseInt(cursorData.sortValue());
                params.addValue("cursorPrice", cursorPrice);
                sql.append("""
                         AND (
                            d.price > :cursorPrice
                            OR (d.price = :cursorPrice AND d.deviceId > :cursorDeviceId)
                         )
                        """);
            }
            case PRICE_DESC -> {
                // price DESC, deviceId DESC
                // (price < cursorPrice) OR (price = cursorPrice AND deviceId < cursorDeviceId)
                Integer cursorPrice = Integer.parseInt(cursorData.sortValue());
                params.addValue("cursorPrice", cursorPrice);
                sql.append("""
                         AND (
                            d.price < :cursorPrice
                            OR (d.price = :cursorPrice AND d.deviceId < :cursorDeviceId)
                         )
                        """);
            }
        }
    }

    // --- Helper Methods for Enriching Specs ---

    private void enrichSmartphones(List<DeviceItemDto> devices, List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return;

        String sql = "SELECT * FROM smartphones WHERE deviceId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        Map<Long, Map<String, Object>> specsMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Map<String, Object> specs = new HashMap<>();
            addIfNotNull(specs, "os", row.get("os"));
            addIfNotNull(specs, "chargingPort", row.get("chargingPort"));
            addIfNotNull(specs, "storageGb", row.get("storageGb"));
            addIfNotNull(specs, "ramGb", row.get("ramGb"));
            addIfNotNull(specs, "screenInch", row.get("screenInch"));
            addIfNotNull(specs, "batteryMah", row.get("batteryMah"));
            specsMap.put(((Number) row.get("deviceId")).longValue(), specs);
        }

        assignSpecs(devices, DeviceType.SMARTPHONE, specsMap);
    }

    private void enrichLaptops(List<DeviceItemDto> devices, List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return;

        String sql = "SELECT * FROM laptops WHERE deviceId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        Map<Long, Map<String, Object>> specsMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Map<String, Object> specs = new HashMap<>();
            addIfNotNull(specs, "os", row.get("os"));
            addIfNotNull(specs, "cpu", row.get("cpu"));
            addIfNotNull(specs, "gpu", row.get("gpu"));
            addIfNotNull(specs, "minRequiredPowerW", row.get("minRequiredPowerW"));
            addIfNotNull(specs, "ramGb", row.get("ramGb"));
            addIfNotNull(specs, "storageGb", row.get("storageGb"));
            addIfNotNull(specs, "screenInch", row.get("screenInch"));
            addIfNotNull(specs, "batteryWh", row.get("batteryWh"));
            specsMap.put(((Number) row.get("deviceId")).longValue(), specs);
        }

        assignSpecs(devices, DeviceType.LAPTOP, specsMap);
    }

    private void enrichTablets(List<DeviceItemDto> devices, List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return;

        String sql = "SELECT * FROM tablets WHERE deviceId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        Map<Long, Map<String, Object>> specsMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Map<String, Object> specs = new HashMap<>();
            addIfNotNull(specs, "os", row.get("os"));
            addIfNotNull(specs, "screenInch", row.get("screenInch"));
            addIfNotNull(specs, "stylusType", row.get("stylusType"));
            addIfNotNull(specs, "chargingPort", row.get("chargingPort"));
            addIfNotNull(specs, "storageGb", row.get("storageGb"));
            addIfNotNull(specs, "ramGb", row.get("ramGb"));
            addIfNotNull(specs, "batteryMah", row.get("batteryMah"));
            specsMap.put(((Number) row.get("deviceId")).longValue(), specs);
        }

        assignSpecs(devices, DeviceType.TABLET, specsMap);
    }

    private void enrichSmartwatches(List<DeviceItemDto> devices, List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return;

        String sql = "SELECT * FROM smartwatches WHERE deviceId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        Map<Long, Map<String, Object>> specsMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Map<String, Object> specs = new HashMap<>();
            addIfNotNull(specs, "os", row.get("os"));
            addIfNotNull(specs, "compatiblePhoneOs", row.get("compatiblePhoneOs"));
            addIfNotNull(specs, "caseSizeMm", row.get("caseSizeMm"));
            addIfNotNull(specs, "hasGps", row.get("hasGps"));
            addIfNotNull(specs, "hasCellular", row.get("hasCellular"));
            addIfNotNull(specs, "battery", row.get("battery"));
            specsMap.put(((Number) row.get("deviceId")).longValue(), specs);
        }

        assignSpecs(devices, DeviceType.SMARTWATCH, specsMap);
    }

    private void enrichAudio(List<DeviceItemDto> devices, List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return;

        String sql = "SELECT * FROM audioDevices WHERE deviceId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        Map<Long, Map<String, Object>> specsMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Map<String, Object> specs = new HashMap<>();
            addIfNotNull(specs, "formFactor", row.get("formFactor"));
            addIfNotNull(specs, "supportedCodecs", row.get("supportedCodecs"));
            addIfNotNull(specs, "connectionType", row.get("connectionType"));
            addIfNotNull(specs, "hasAnc", row.get("hasAnc"));
            addIfNotNull(specs, "batteryLifeHours", row.get("batteryLifeHours"));
            specsMap.put(((Number) row.get("deviceId")).longValue(), specs);
        }

        assignSpecs(devices, DeviceType.AUDIO, specsMap);
    }

    private void enrichKeyboards(List<DeviceItemDto> devices, List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return;

        String sql = "SELECT * FROM keyboards WHERE deviceId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        Map<Long, Map<String, Object>> specsMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Map<String, Object> specs = new HashMap<>();
            addIfNotNull(specs, "supportedLayouts", row.get("supportedLayouts"));
            addIfNotNull(specs, "connectionType", row.get("connectionType"));
            addIfNotNull(specs, "keyboardSize", row.get("keyboardSize"));
            addIfNotNull(specs, "switchType", row.get("switchType"));
            addIfNotNull(specs, "multiPairingCount", row.get("multiPairingCount"));
            addIfNotNull(specs, "hasBacklight", row.get("hasBacklight"));
            specsMap.put(((Number) row.get("deviceId")).longValue(), specs);
        }

        assignSpecs(devices, DeviceType.KEYBOARD, specsMap);
    }

    private void enrichMice(List<DeviceItemDto> devices, List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return;

        String sql = "SELECT * FROM mice WHERE deviceId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        Map<Long, Map<String, Object>> specsMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Map<String, Object> specs = new HashMap<>();
            addIfNotNull(specs, "connectionType", row.get("connectionType"));
            addIfNotNull(specs, "gestureSupport", row.get("gestureSupport"));
            addIfNotNull(specs, "powerSource", row.get("powerSource"));
            addIfNotNull(specs, "mouseType", row.get("mouseType"));
            addIfNotNull(specs, "maxDpi", row.get("maxDpi"));
            addIfNotNull(specs, "buttonCount", row.get("buttonCount"));
            addIfNotNull(specs, "multiPairingCount", row.get("multiPairingCount"));
            specsMap.put(((Number) row.get("deviceId")).longValue(), specs);
        }

        assignSpecs(devices, DeviceType.MOUSE, specsMap);
    }

    private void enrichChargers(List<DeviceItemDto> devices, List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return;

        String sql = "SELECT * FROM chargers WHERE deviceId IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        Map<Long, Map<String, Object>> specsMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Map<String, Object> specs = new HashMap<>();
            addIfNotNull(specs, "totalPowerW", row.get("totalPowerW"));
            addIfNotNull(specs, "maxSinglePortPowerW", row.get("maxSinglePortPowerW"));
            addIfNotNull(specs, "portConfiguration", row.get("portConfiguration"));
            addIfNotNull(specs, "supportedProtocols", row.get("supportedProtocols"));
            addIfNotNull(specs, "chargerType", row.get("chargerType"));
            addIfNotNull(specs, "isGan", row.get("isGan"));
            specsMap.put(((Number) row.get("deviceId")).longValue(), specs);
        }

        assignSpecs(devices, DeviceType.CHARGER, specsMap);
    }

    /**
     * DTO 리스트에 조회된 Specs 할당 (불변 객체 유지를 위해 toBuilder 사용)
     */
    private void assignSpecs(List<DeviceItemDto> devices, DeviceType type, Map<Long, Map<String, Object>> specsMap) {
        for (int i = 0; i < devices.size(); i++) {
            DeviceItemDto device = devices.get(i);
            if (device.getDeviceType() == type) {
                devices.set(i, device.toBuilder()
                        .specifications(specsMap.get(device.getDeviceId()))
                        .build());
            }
        }
    }

    private void addIfNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
