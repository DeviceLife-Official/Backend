package com.devicelife.devicelife_api.repository.lifestyle;

import com.devicelife.devicelife_api.domain.lifestyle.request.DeviceSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeviceSearchRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public List<DeviceSearchDto> searchByKeyword(String keyword, int limit) {
        String sql = """
            SELECT d.deviceId, CONCAT(b.brandName, ' ', d.name) AS displayName
            FROM devices d
            JOIN brands b ON b.brandId = d.brandId
            WHERE d.name LIKE :kw OR b.brandName LIKE :kw
            ORDER BY d.deviceId DESC
            LIMIT :limit
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("kw", "%" + keyword + "%")
                .addValue("limit", limit);

        return jdbc.query(sql, params, (rs, n) ->
                new DeviceSearchDto(rs.getLong("deviceId"), rs.getString("displayName"))
        );
    }
}