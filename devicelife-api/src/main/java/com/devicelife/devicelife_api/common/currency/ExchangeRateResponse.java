package com.devicelife.devicelife_api.common.currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * ExchangeRate-API 응답 DTO
 * API 문서: https://www.exchangerate-api.com/docs/overview
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {
    
    /**
     * 응답 결과 (success, error)
     */
    @JsonProperty("result")
    private String result;
    
    /**
     * 기준 통화 (예: KRW)
     */
    @JsonProperty("base_code")
    private String baseCode;
    
    /**
     * 환율 맵 (통화 코드 -> 환율)
     * 예: {"USD": 0.00077, "EUR": 0.00071, ...}
     */
    @JsonProperty("conversion_rates")
    private Map<String, BigDecimal> conversionRates;
    
    /**
     * 마지막 업데이트 시간 (Unix timestamp)
     */
    @JsonProperty("time_last_update_unix")
    private Long timeLastUpdateUnix;
    
    /**
     * 다음 업데이트 시간 (Unix timestamp)
     */
    @JsonProperty("time_next_update_unix")
    private Long timeNextUpdateUnix;
}
