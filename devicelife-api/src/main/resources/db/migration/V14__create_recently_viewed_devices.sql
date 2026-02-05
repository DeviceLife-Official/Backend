-- 최근 본 기기 테이블 생성
CREATE TABLE IF NOT EXISTS recentlyViewedDevices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT NOT NULL,
    deviceId BIGINT NOT NULL,
    viewedAt DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    
    UNIQUE KEY uk_user_device (userId, deviceId),
    
    CONSTRAINT fk_recently_viewed_user 
        FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE,
    CONSTRAINT fk_recently_viewed_device 
        FOREIGN KEY (deviceId) REFERENCES devices(deviceId) ON DELETE CASCADE,
    
    INDEX idx_user_viewed_at (userId, viewedAt DESC)
);
