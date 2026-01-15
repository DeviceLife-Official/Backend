package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 생체 인식 방식
 */
@Getter
@RequiredArgsConstructor
public enum BiometricType {

    FACE_ID("Face ID"),
    FINGERPRINT("지문 인식 (측면/후면)"),
    ON_SCREEN_FINGERPRINT("화면 내 지문 인식"),
    NONE("미지원");

    private final String displayName;
}
