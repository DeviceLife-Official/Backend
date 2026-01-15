package com.devicelife.devicelife_api.repository.lifestyle;

import com.devicelife.devicelife_api.domain.lifestyle.request.LifestyleFeaturedDeviceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LifestyleFeaturedRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public LifestyleTagRow findTagByKey(String tagKey) {
        String sql = """
            SELECT tagId, tagKey, tagLabel, tagType
            FROM tags
            WHERE tagKey = :tagKey
        """;
        List<LifestyleTagRow> rows = jdbc.query(sql, new MapSqlParameterSource("tagKey", tagKey),
                (rs, n) -> new LifestyleTagRow(
                        rs.getLong("tagId"),
                        rs.getString("tagKey"),
                        rs.getString("tagLabel"),
                        rs.getString("tagType")
                ));
        return rows.isEmpty() ? null : rows.get(0);
    }

    /**
     * 태그별 Featured Set upsert 후 featuredSetId 반환
     */
    public Long upsertFeaturedSet(Long tagId) {
        String upsert = """
            INSERT INTO lifestyleFeaturedSets (tagId, isActive)
            VALUES (:tagId, 1)
            ON DUPLICATE KEY UPDATE
                isActive = 1,
                updatedAt = CURRENT_TIMESTAMP
        """;
        jdbc.update(upsert, new MapSqlParameterSource("tagId", tagId));

        String selectId = """
            SELECT featuredSetId
            FROM lifestyleFeaturedSets
            WHERE tagId = :tagId
        """;
        List<Long> ids = jdbc.query(selectId, new MapSqlParameterSource("tagId", tagId),
                (rs, n) -> rs.getLong("featuredSetId"));
        return ids.isEmpty() ? null : ids.get(0);
    }

    public void replaceSetDevices(Long featuredSetId, List<Long> deviceIds3) {
        String delete = "DELETE FROM lifestyleFeaturedSetDevices WHERE featuredSetId = :featuredSetId";
        jdbc.update(delete, new MapSqlParameterSource("featuredSetId", featuredSetId));

        String insert = """
            INSERT INTO lifestyleFeaturedSetDevices (featuredSetId, slot, deviceId)
            VALUES (:featuredSetId, :slot, :deviceId)
        """;

        MapSqlParameterSource[] batch = new MapSqlParameterSource[3];
        for (int i = 0; i < 3; i++) {
            batch[i] = new MapSqlParameterSource()
                    .addValue("featuredSetId", featuredSetId)
                    .addValue("slot", i + 1)
                    .addValue("deviceId", deviceIds3.get(i));
        }
        jdbc.batchUpdate(insert, batch);
    }

    public boolean existsAllDevices(List<Long> deviceIds) {
        String sql = """
            SELECT COUNT(*) AS cnt
            FROM devices
            WHERE deviceId IN (:ids)
        """;
        Integer cnt = jdbc.queryForObject(sql, new MapSqlParameterSource("ids", deviceIds), Integer.class);
        return cnt != null && cnt == deviceIds.size();
    }

    public List<LifestyleFeaturedDeviceDto> findFeaturedDevicesByTagKey(String tagKey) {
        String sql = """
            SELECT
                sd.slot AS slot,
                d.deviceId AS deviceId,
                COALESCE(d.imageUrl, img.imageUrl) AS imageUrl,
                CONCAT(b.brandName, ' ', d.name) AS displayName,
                d.releaseDate AS releaseDate,
                COALESCE(MIN(o.price), d.price) AS price
            FROM tags t
            JOIN lifestyleFeaturedSets s
              ON s.tagId = t.tagId AND s.isActive = 1
            JOIN lifestyleFeaturedSetDevices sd
              ON sd.featuredSetId = s.featuredSetId
            JOIN devices d
              ON d.deviceId = sd.deviceId
            JOIN brands b
              ON b.brandId = d.brandId
            LEFT JOIN deviceOffers o
              ON o.deviceId = d.deviceId AND o.price IS NOT NULL
            LEFT JOIN (
                SELECT di1.deviceId, di1.imageUrl
                FROM deviceImages di1
                JOIN (
                    SELECT deviceId, MIN(sortOrder) AS minSort
                    FROM deviceImages
                    GROUP BY deviceId
                ) m ON m.deviceId = di1.deviceId AND m.minSort = di1.sortOrder
            ) img ON img.deviceId = d.deviceId
            WHERE t.tagKey = :tagKey
            GROUP BY sd.slot, d.deviceId, d.imageUrl, img.imageUrl, b.brandName, d.name, d.releaseDate, d.price
            ORDER BY sd.slot ASC
        """;

        return jdbc.query(sql, new MapSqlParameterSource("tagKey", tagKey), (rs, n) -> {
            LocalDate releaseDate = rs.getObject("releaseDate", LocalDate.class);
            Integer price = (Integer) rs.getObject("price");
            return new LifestyleFeaturedDeviceDto(
                    rs.getInt("slot"),
                    rs.getLong("deviceId"),
                    rs.getString("imageUrl"),
                    rs.getString("displayName"),
                    releaseDate,
                    price,
                    "KRW"
            );
        });
    }

    public record LifestyleTagRow(Long tagId, String tagKey, String tagLabel, String tagType) {}
}
