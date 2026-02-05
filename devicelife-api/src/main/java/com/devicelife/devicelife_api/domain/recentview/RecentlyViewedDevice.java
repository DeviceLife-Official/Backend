package com.devicelife.devicelife_api.domain.recentview;

import com.devicelife.devicelife_api.domain.device.Device;
import com.devicelife.devicelife_api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 최근 본 기기 엔티티
 * 사용자가 조회한 기기의 기록을 저장합니다.
 */
@Entity
@Table(name = "recentlyViewedDevices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "deviceId"}))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentlyViewedDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userId", nullable = false, insertable = false, updatable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "deviceId", nullable = false, insertable = false, updatable = false)
    private Long deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceId", nullable = false)
    private Device device;

    /**
     * 조회 시간
     */
    @Column(name = "viewedAt", nullable = false)
    private LocalDateTime viewedAt;

    @PrePersist
    protected void onCreate() {
        if (viewedAt == null) {
            viewedAt = LocalDateTime.now();
        }
    }

    /**
     * 조회 시간 업데이트 (동일 기기 재조회 시)
     */
    public void updateViewedAt() {
        this.viewedAt = LocalDateTime.now();
    }
}
