package com.devicelife.devicelife_api.repository.device;

import com.devicelife.devicelife_api.domain.device.Brand;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 브랜드 Repository
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT DISTINCT b " +
            "FROM Brand b " +
            "JOIN Device d ON b.brandId = d.brandId " +
            "WHERE d.deviceType = :deviceType " +
            "ORDER BY b.brandName")
    List<Brand> findByDeviceType(@Param("deviceType") DeviceType deviceType);

    /**
     * 필터 조건이 적용된 Device 개수 기준 상위 8개 브랜드 조회
     * DeviceSearchCustomRepositoryImpl의 필터 조건과 동일한 조건을 적용합니다.
     */
    @Query(value = """
            SELECT b.* FROM brands b
            JOIN devices d ON b.brandId = d.brandId
            WHERE (
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
            GROUP BY b.brandId
            ORDER BY COUNT(d.deviceId) DESC
            LIMIT 8
            """, nativeQuery = true)
    List<Brand> findTopBrandsByFilteredDeviceCount();
}
