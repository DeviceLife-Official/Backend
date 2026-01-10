package com.devicelife.devicelife_api.common.response;

import lombok.Getter;

/**
 * 성공 응답 코드 예시
 * 각 도메인별로 필요한 성공 코드를 정의하여 사용
 */
@Getter
public enum SuccessCode {

    // 예시: 사용자
    USER_2001("USER_2001", "회원가입에 성공했습니다."),
    USER_2002("USER_2002", "로그인에 성공했습니다."),
    USER_2003("USER_2003", "사용자 정보 조회에 성공했습니다."),

    // 예시: 디바이스
    DEVICE_2001("DEVICE_2001", "디바이스 목록 조회에 성공했습니다."),
    DEVICE_2002("DEVICE_2002", "디바이스 상세 조회에 성공했습니다."),

    COMBO_CREATE_SUCCESS("COMBO_2000", "조합 생성에 성공했습니다."),

    TAG_GET_SUCCESS("TAG_2000", "태그 목록 조회에 성공했습니다."),
    TAG_SAVE_SUCCESS("TAG_2001", "유저 태그 저장에 성공했습니다."),

    ONBOARDING_COMPLETE_SUCCESS("ONBOARDING_2000", "온보딩 완료 처리에 성공했습니다."),

    LIFESTYLE_GET_SUCCESS("LIFESTYLE_2000", "라이프스타일 추천 조회에 성공했습니다.");


    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

