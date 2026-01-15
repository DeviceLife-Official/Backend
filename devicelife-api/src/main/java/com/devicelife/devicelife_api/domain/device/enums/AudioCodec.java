package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 오디오 코덱 종류
 */
@Getter
@RequiredArgsConstructor
public enum AudioCodec {

    SBC("SBC", "기본 코덱", false),
    AAC("AAC", "Apple 최적화 코덱", true),
    APTX("aptX", "Qualcomm 코덱", true),
    APTX_HD("aptX HD", "Qualcomm 고음질 코덱", true),
    APTX_ADAPTIVE("aptX Adaptive", "Qualcomm 적응형 코덱", true),
    LDAC("LDAC", "Sony 고음질 코덱", true),
    SSC("SSC", "Samsung Scalable Codec", true),
    LC3("LC3", "Bluetooth LE Audio 코덱", true),
    LHDC("LHDC", "HiRes 무선 코덱", true);

    private final String displayName;
    private final String description;
    private final boolean hiRes;

    /**
     * Apple 기기 최적화 코덱인지 확인
     */
    public boolean isAppleOptimized() {
        return this == AAC;
    }

    /**
     * Android 기기 최적화 코덱인지 확인
     */
    public boolean isAndroidOptimized() {
        return this == LDAC || this == APTX || this == APTX_HD 
            || this == APTX_ADAPTIVE || this == SSC;
    }
}
