package com.devicelife.devicelife_api.common.currency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 실시간 환율 조회 서비스
 * ExchangeRate-API를 사용하여 환율 정보를 가져옵니다.
 * 
 * API 호출 실패 시 Fallback 환율(2026.01.27 기준 실제 환율) 자동 사용
 * 
 * 무료 플랜: 월 1,500 요청
 * API 문서: https://www.exchangerate-api.com/docs/overview
 */
@Slf4j
@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl;
    
    // 환율 캐시 (통화 코드 -> 1 KRW당 환율)
    // 예: {"USD": 0.00077, "EUR": 0.00071} = 1원 = 0.00077달러
    private final Map<String, BigDecimal> exchangeRateCache = new ConcurrentHashMap<>();
    
    // 역환율 캐시 (통화 코드 -> 해당 통화 1단위당 KRW)
    // 예: {"USD": 1300, "EUR": 1400} = 1달러 = 1300원
    private final Map<String, BigDecimal> reverseExchangeRateCache = new ConcurrentHashMap<>();
    
    // 고정 환율 (fallback용)
    // 2026년 1월 27일 기준 실제 환율
    private static final Map<String, BigDecimal> FALLBACK_RATES = new HashMap<>();
    
    static {
        FALLBACK_RATES.put("KRW", BigDecimal.ONE);           // 원화 (기준)
        FALLBACK_RATES.put("USD", new BigDecimal("1475"));   // 미국 달러 (1,472-1,477 중간값)
        FALLBACK_RATES.put("EUR", new BigDecimal("1693"));   // 유로 (1월 평균)
        FALLBACK_RATES.put("JPY", new BigDecimal("9.23"));   // 일본 엔 (1월 평균)
        FALLBACK_RATES.put("CNY", new BigDecimal("212"));    // 중국 위안 (211.80-212.33 중간값)
        FALLBACK_RATES.put("GBP", new BigDecimal("1951"));   // 영국 파운드 (1월 평균)
    }

    public ExchangeRateService(
            RestTemplate restTemplate,
            @Value("${currency.exchange-rate-api.key:}") String apiKey,
            @Value("${currency.exchange-rate-api.url:https://v6.exchangerate-api.com/v6}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        
        // 초기화 시 고정 환율로 캐시 채우기
        reverseExchangeRateCache.putAll(FALLBACK_RATES);
        
        log.info("ExchangeRateService initialized. API Key configured: {}", !apiKey.isBlank());
    }

    /**
     * 환율 정보를 갱신합니다.
     * KRW를 기준으로 다른 통화의 환율을 가져옵니다.
     * 
     * @return 갱신 성공 여부
     */
    public boolean refreshExchangeRates() {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Exchange rate API key is not configured. Using fallback rates.");
            return false;
        }

        try {
            String url = String.format("%s/%s/latest/KRW", apiUrl, apiKey);
            log.info("Fetching exchange rates from: {}", url);
            
            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
            
            if (response != null && "success".equals(response.getResult())) {
                Map<String, BigDecimal> rates = response.getConversionRates();
                
                if (rates != null && !rates.isEmpty()) {
                    // 정방향 환율 저장 (1 KRW -> 다른 통화)
                    exchangeRateCache.clear();
                    exchangeRateCache.putAll(rates);
                    
                    // 역방향 환율 계산 및 저장 (다른 통화 -> KRW)
                    reverseExchangeRateCache.clear();
                    reverseExchangeRateCache.put("KRW", BigDecimal.ONE);
                    
                    rates.forEach((currency, rate) -> {
                        if (rate.compareTo(BigDecimal.ZERO) > 0) {
                            // 역환율 = 1 / rate
                            BigDecimal reverseRate = BigDecimal.ONE.divide(rate, 4, RoundingMode.HALF_UP);
                            reverseExchangeRateCache.put(currency, reverseRate);
                        }
                    });
                    
                    log.info("Exchange rates updated successfully. Total currencies: {}", rates.size());
                    log.debug("Sample rates - USD: {}, EUR: {}, JPY: {}", 
                            reverseExchangeRateCache.get("USD"),
                            reverseExchangeRateCache.get("EUR"),
                            reverseExchangeRateCache.get("JPY"));
                    
                    return true;
                }
            }
            
            log.warn("Failed to fetch exchange rates. Response: {}", response);
            return false;
            
        } catch (Exception e) {
            log.error("Error fetching exchange rates from API", e);
            return false;
        }
    }

    /**
     * 특정 통화를 KRW로 변환하는 환율을 가져옵니다.
     * 
     * @param currency 통화 코드 (USD, EUR 등)
     * @return 해당 통화 1단위당 KRW 환율
     */
    public BigDecimal getExchangeRateToKRW(String currency) {
        if (currency == null || currency.isBlank() || "KRW".equalsIgnoreCase(currency)) {
            return BigDecimal.ONE;
        }
        
        // 캐시에서 환율 가져오기
        BigDecimal rate = reverseExchangeRateCache.get(currency.toUpperCase());
        
        if (rate != null) {
            return rate;
        }
        
        // 캐시에 없으면 fallback 환율 사용
        rate = FALLBACK_RATES.get(currency.toUpperCase());
        if (rate != null) {
            log.warn("Using fallback exchange rate for {}: {}", currency, rate);
            return rate;
        }
        
        // 그것도 없으면 1:1로 반환 (경고 로그)
        log.warn("No exchange rate found for currency: {}. Using 1:1 rate.", currency);
        return BigDecimal.ONE;
    }

    /**
     * 캐시된 환율 정보가 있는지 확인합니다.
     * 
     * @return 캐시 여부
     */
    public boolean hasCachedRates() {
        return !exchangeRateCache.isEmpty();
    }

    /**
     * 지원하는 모든 통화 목록을 반환합니다.
     * 
     * @return 통화 코드 배열
     */
    public String[] getSupportedCurrencies() {
        return reverseExchangeRateCache.keySet().toArray(new String[0]);
    }
}
