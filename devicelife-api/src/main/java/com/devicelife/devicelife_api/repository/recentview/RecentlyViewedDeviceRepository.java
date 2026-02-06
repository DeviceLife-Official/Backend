package com.devicelife.devicelife_api.repository.recentview;

import com.devicelife.devicelife_api.domain.recentview.RecentlyViewedDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecentlyViewedDeviceRepository extends JpaRepository<RecentlyViewedDevice, Long> {

    /**
     * 사용자의 최근 본 기기 목록 조회 (최신순, 상위 2개)
     */
    List<RecentlyViewedDevice> findTop2ByUserIdOrderByViewedAtDesc(Long userId);

    /**
     * 사용자가 특정 기기를 조회한 기록 조회
     */
    Optional<RecentlyViewedDevice> findByUserIdAndDeviceId(Long userId, Long deviceId);

    /**
     * 사용자의 최근 본 기기 개수 조회
     */
    long countByUserId(Long userId);
}
