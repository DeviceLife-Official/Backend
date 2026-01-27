package com.devicelife.devicelife_api.scheduler;

import com.devicelife.devicelife_api.common.currency.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 환율 갱신 스케줄러
 * 
 * - 앱 시작 시 즉시 환율 갱신
 * - 이후 6시간마다 자동 갱신
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateScheduler {

    private final ExchangeRateService exchangeRateService;

    /**
     * 애플리케이션 시작 시 환율 갱신
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Application started. Initializing exchange rates...");
        refreshExchangeRates();
    }

    /**
     * 6시간마다 환율 갱신
     * ExchangeRate-API 무료 플랜은 월 1,500 요청까지 가능
     * 6시간마다 갱신 = 하루 4회 = 월 120회 (충분)
     */
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // 6시간 = 21,600,000ms
    public void refreshExchangeRatesScheduled() {
        log.info("Scheduled exchange rate refresh triggered");
        refreshExchangeRates();
    }

    /**
     * 환율 갱신 실행
     */
    private void refreshExchangeRates() {
        try {
            boolean success = exchangeRateService.refreshExchangeRates();
            
            if (success) {
                log.info("✅ Exchange rates refreshed successfully");
            } else {
                log.warn("⚠️ Failed to refresh exchange rates. Using fallback rates.");
            }
            
        } catch (Exception e) {
            log.error("❌ Error refreshing exchange rates", e);
        }
    }
}
