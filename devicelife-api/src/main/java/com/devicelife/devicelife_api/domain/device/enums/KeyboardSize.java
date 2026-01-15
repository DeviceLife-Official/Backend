package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 키보드 배열 크기
 */
@Getter
@RequiredArgsConstructor
public enum KeyboardSize {

    FULL("풀배열 (104키)", 104),
    TKL("텐키리스 (87키)", 87),
    COMPACT_75("75%", 84),
    COMPACT_65("65%", 68),
    MINI_60("미니 (60%)", 61);

    private final String displayName;
    private final int approximateKeys;

    public boolean hasNumpad() {
        return this == FULL;
    }
}
