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
}
