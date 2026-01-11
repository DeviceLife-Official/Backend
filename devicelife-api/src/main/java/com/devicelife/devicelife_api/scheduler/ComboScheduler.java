package com.devicelife.devicelife_api.scheduler;

import com.devicelife.devicelife_api.service.combo.ComboService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 조합 관련 스케줄러
 * 
 * - 30일 지난 휴지통 조합 자동 삭제
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ComboScheduler {

    private final ComboService comboService;

    /**
     * 30일 지난 조합 자동 삭제
     * 매일 새벽 2시에 실행
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void autoDeleteExpiredCombos() {
        log.info("=== 조합 자동 삭제 스케줄러 시작 ===");
        try {
            comboService.autoDeleteExpiredCombos();
            log.info("=== 조합 자동 삭제 스케줄러 완료 ===");
        } catch (Exception e) {
            log.error("=== 조합 자동 삭제 스케줄러 실패 ===", e);
        }
    }
}
