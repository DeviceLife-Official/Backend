package com.devicelife.devicelife_api.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {


    // 인증/Auth (AUTH_XXXX)
    AUTH_4011("AUTH_4011", 401, "인증 정보가 누락되었습니다."),
    AUTH_4012("AUTH_4012", 401, "유효하지 않은 토큰입니다."),
    AUTH_4013("AUTH_4013", 401, "이미 로그아웃 처리되었거나 존재하지 않은 토큰입니다."),
    AUTH_4191("AUTH_4191", 419, "토큰이 만료되었습니다."),
    AUTH_4031("AUTH_4031", 403, "접근 권한이 없습니다."),

    // 요청/파라미터 오류 (REQ_XXXX)
    REQ_4001("REQ_4001", 400, "필수 파라미터가 누락되었습니다"),
    REQ_4002("REQ_4002", 400, "파라미터 형식이 잘못되었습니다"),
    REQ_4151("REQ_4151", 415, "지원하지 않는 Content-Type입니다"),

    // API/라우팅 (API_XXXX)
    API_4041("API_4041", 404, "존재하지 않는 API입니다"),
    API_4051("API_4051", 405, "지원하지 않는 HTTP 메서드입니다"),

    // Combo 관련 오류 (COMBO_XXXX)
    COMBO_4041("COMBO_4041", 404, "조합을 찾을 수 없습니다."),
    COMBO_4042("COMBO_4042", 404, "이미 삭제된 조합입니다."),
    COMBO_4001("COMBO_4001", 400, "조합명은 필수입니다."),
    COMBO_4002("COMBO_4002", 400, "이미 휴지통에 있는 조합입니다."),
    COMBO_4003("COMBO_4003", 400, "조합에 이미 해당 기기가 있습니다."),
    COMBO_4004("COMBO_4004", 400, "조합에 해당 기기가 없습니다."),
    COMBO_4005("COMBO_4005", 400, "이미 동일한 이름의 조합이 존재합니다."),

    // Evaluation 관련 오류 (EVAL_XXXX)
    EVAL_4041("EVAL_4041", 404, "평가 데이터를 찾을 수 없습니다."),

    // Tag 관련 오류 (TAG_XXXX)
    TAG_4001("TAG_4001",400,"존재하지 않는 태그가 포함되어있습니다."),

    // Device 관련 오류 (DEVICE_XXXX)
    DEVICE_4041("DEVICE_4041", 404, "기기를 찾을 수 없습니다."),
    DEVICE_4002("DEVICE_4002", 400, "유효하지 않은 커서입니다."),

    // User 관련 오류 (USER_XXXX)
    USER_4041("USER_4041", 404, "사용자를 찾을 수 없습니다."),
    USER_4001("USER_4001", 400, "이미 중복되는 사용자입니다."),
    USER_4002("USER_4002", 400, "email은 비어있을 수 없습니다."),
    USER_4003("USER_4003", 400, "소셜 유저는 email을 수정할 수 없습니다."),
    USER_4004("USER_4004", 400, "닉네임은 빈칸이면 안됩니다."),
    USER_4005("USER_4005", 400, "소셜 로그인으로 신규 가입한 유저는 비밀번호가 존재하지 않습니다."),
    USER_4006("USER_4006", 400, "기존 비밀번호가 일치하지 않습니다."),
    USER_4007("USER_4007", 400, "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다."),
    USER_4008("USER_4008", 400, "새 비밀번호는 기존 비밀번호와 다르게 설정해주세요."),
    USER_4009("USER_4009", 400, "이미 중복되는 email입니다."),


    // 내부 서버 오류 (SERVER_XXXX)
    SERVER_5001("SERVER_5001", 500, "서버 내부 오류입니다."),
    SERVER_5041("SERVER_5041", 504, "외부 서비스 응답 지연"),
    SERVER_5031("SERVER_5031", 503, "서버가 일시적으로 불안정합니다"),

    // Notice 관련 오류 (NOTICE_XXXX)
    NOTICE_4041("NOTICE_4041", 404, "공지를 찾을 수 없습니다."),

    // Faq 관련 오류(FAQ_XXXX)
    FAQ_4041("FAQ_4041", 404, "FAQ를 찾을 수 없습니다."),

    // Policy 관련 오류(POLICY_XXXX)
    POLICY_4041("POLICY_4041", 404, "약관을 찾을 수 없습니다."),

    // Inquiry 관련 오류(INQUIRY_XXXX)
    INQUIRY_4041("INQUIRY_4041", 404, "문의를 찾을 수 없습니다."),

    // Mail 관련 오류(MAIL_XXXX)
    MAIL_4001("MAIL_4001",400,"인증번호 재전송 횟수를 초과했습니다. 잠시 후 다시 시도해 주세요."),
    MAIL_4002("MAIL_4002",400,"너무 자주 요청했습니다. 잠시 후 다시 시도해 주세요."),
    MAIL_4003("MAIL_4003",400,"인증번호가 만료되었습니다. 재전송해 주세요."),
    MAIL_4004("MAIL_4004",400,"인증번호 입력 시도 횟수를 초과했습니다. 재전송해 주세요."),
    MAIL_4005("MAIL_4005",400,"인증번호가 일치하지 않습니다."),
    MAIL_5001("MAIL_5001",500,"메일 전송 실패하였습니다."),
    MAIL_4191("MAIL_4191", 419, "세션이 만료되었거나 잘못된 접근입니다.")
    ;

    private final String code;
    private final int status;
    private final String message;

    ErrorCode(String code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
