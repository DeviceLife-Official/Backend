package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 마우스 형태
 */
@Getter
@RequiredArgsConstructor
public enum MouseType {

    STANDARD("일반형"),
    VERTICAL("버티컬 (인체공학)"),
    TRACKBALL("트랙볼"),
    GAMING("게이밍"),
    COMPACT("컴팩트 (휴대용)");

    private final String displayName;

    public boolean isErgonomic() {
        return this == VERTICAL || this == TRACKBALL;
    }
}
