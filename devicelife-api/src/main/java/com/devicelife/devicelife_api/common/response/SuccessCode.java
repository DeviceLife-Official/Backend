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

    // 예시: 콤보
    COMBO_2001("COMBO_2001", "콤보 생성에 성공했습니다."),
    COMBO_2002("COMBO_2002", "콤보 조회에 성공했습니다."),
    COMBO_2003("COMBO_2003", "콤보 수정에 성공했습니다."),
    COMBO_2004("COMBO_2004", "콤보 삭제에 성공했습니다.");

    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

