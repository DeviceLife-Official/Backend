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

    // Combo
    COMBO_CREATE_SUCCESS("COMBO_2000", "조합 생성에 성공했습니다."),
    COMBO_GET_SUCCESS("COMBO_2001", "조합 조회에 성공했습니다."),
    COMBO_LIST_SUCCESS("COMBO_2002", "조합 목록 조회에 성공했습니다."),
    COMBO_UPDATE_SUCCESS("COMBO_2003", "조합 수정에 성공했습니다."),
    COMBO_DELETE_SUCCESS("COMBO_2004", "조합 삭제에 성공했습니다."),
    COMBO_RESTORE_SUCCESS("COMBO_2005", "조합 복구에 성공했습니다."),
    COMBO_PERMANENT_DELETE_SUCCESS("COMBO_2006", "조합 영구 삭제에 성공했습니다."),
    COMBO_PIN_SUCCESS("COMBO_2007", "조합 즐겨찾기에 성공했습니다."),
    COMBO_UNPIN_SUCCESS("COMBO_2008", "조합 즐겨찾기 해제에 성공했습니다."),
    COMBO_DEVICE_ADD_SUCCESS("COMBO_2009", "조합에 기기 추가에 성공했습니다."),
    COMBO_DEVICE_REMOVE_SUCCESS("COMBO_2010", "조합에서 기기 삭제에 성공했습니다."),
    COMBO_TRASH_LIST_SUCCESS("COMBO_2011", "휴지통 목록 조회에 성공했습니다."),

    TAG_GET_SUCCESS("TAG_2000", "태그 목록 조회에 성공했습니다."),
    TAG_SAVE_SUCCESS("TAG_2001", "유저 태그 저장에 성공했습니다."),

    ONBOARDING_COMPLETE_SUCCESS("ONBOARDING_2000", "온보딩 완료 처리에 성공했습니다."),

    LIFESTYLE_GET_SUCCESS("LIFESTYLE_2000", "라이프스타일 추천 조회에 성공했습니다."),

    NOTICE_CREATE_SUCCESS("NOTICE_2000", "공지 생성에 성공했습니다."),
    NOTICE_GET_SUCCESS("NOTICE_2001", "공지 조회에 성공했습니다."),
    NOTICE_LIST_SUCCESS("NOTICE_2002", "공지 목록 조회에 성공했습니다."),
    NOTICE_UPDATE_SUCCESS("NOTICE_2003", "공지 수정에 성공했습니다."),
    NOTICE_DELETE_SUCCESS("NOTICE_2004", "공지 삭제에 성공했습니다."),

    FAQ_CREATE_SUCCESS("FAQ_2000", "FAQ 생성에 성공했습니다."),
    FAQ_GET_SUCCESS("FAQ_2001", "FAQ 조회에 성공했습니다."),
    FAQ_UPDATE_SUCCESS("FAQ_2002", "FAQ 수정에 성공했습니다."),
    FAQ_DELETE_SUCCESS("FAQ_2003", "FAQ 삭제에 성공했습니다."),

    POLICY_CREATE_SUCCESS("POLICY_2000", "약관 생성에 성공했습니다."),
    POLICY_GET_SUCCESS("POLICY_2001", "약관 조회에 성공했습니다."),
    POLICY_LIST_SUCCESS("POLICY_2002", "약관 목록 조회에 성공했습니다."),
    POLICY_UPDATE_SUCCESS("POLICY_2003", "약관 수정에 성공했습니다."),
    POLICY_DELETE_SUCCESS("POLICY_2004", "약관 삭제에 성공했습니다."),
    ;


    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

