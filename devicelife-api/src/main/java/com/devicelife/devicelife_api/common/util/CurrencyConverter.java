package com.devicelife.devicelife_api.common.util;

import com.devicelife.devicelife_api.common.currency.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 통화 변환 유틸리티
 * 다양한 통화를 KRW(원화)로 변환합니다.
 * 
 * ExchangeRateService를 통해 실시간 환율 정보를 사용합니다.
 */
@Component
@RequiredArgsConstructor
public class CurrencyConverter {

    private final ExchangeRateService exchangeRateService;

    /**
     * 주어진 가격과 통화를 KRW로 변환합니다.
     * 
     * @param price 가격
     * @param currency 통화 코드 (KRW, USD, EUR 등)
     * @return KRW로 변환된 가격 (정수형)
     */
    public Integer convertToKRW(Integer price, String currency) {
        // 가격이 null이면 0 반환
        if (price == null) {
            return 0;
        }
        
        // 통화가 null이거나 빈 문자열이면 KRW로 간주
        if (currency == null || currency.isBlank()) {
            return price;
        }
        
        // 이미 KRW인 경우 그대로 반환
        if ("KRW".equalsIgnoreCase(currency)) {
            return price;
        }
        
        // ExchangeRateService에서 환율 가져오기
        BigDecimal exchangeRate = exchangeRateService.getExchangeRateToKRW(currency);
        
        // 환율 계산: 가격 × 환율
        BigDecimal priceDecimal = new BigDecimal(price);
        BigDecimal convertedPrice = priceDecimal.multiply(exchangeRate);
        
        // 정수로 반올림하여 반환
        return convertedPrice.setScale(0, RoundingMode.HALF_UP).intValue();
    }
    
    /**
     * 지원하는 모든 통화 목록을 반환합니다.
     * 
     * @return 통화 코드 배열
     */
    public String[] getSupportedCurrencies() {
        return exchangeRateService.getSupportedCurrencies();
    }
}
