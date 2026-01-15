package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 키보드 스위치(축) 종류
 */
@Getter
@RequiredArgsConstructor
public enum KeyboardSwitchType {

    BLUE("청축", "클릭감, 소음 있음"),
    RED("적축", "리니어, 부드러움"),
    BROWN("갈축", "택타일, 적당한 피드백"),
    BLACK("흑축", "리니어, 무거움"),
    SILVER("은축", "리니어, 빠른 반응"),
    SCISSOR("펜타그래프 (시저)", "얇은 키캡, 노트북용"),
    TOPRE("무접점 (토프레)", "정전용량 방식"),
    OPTICAL("광축", "광학 스위치"),
    LOW_PROFILE("로우 프로파일", "낮은 키캡");

    private final String displayName;
    private final String description;

    public boolean isMechanical() {
        return this == BLUE || this == RED || this == BROWN 
            || this == BLACK || this == SILVER;
    }
}
