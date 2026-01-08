package com.devicelife.devicelife_api.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 인증/Auth (USER_XXXX, AUTH_XXXX)
    AUTH_4011("AUTH_4011", 401, "인증 정보가 누락되었습니다."),
    AUTH_4012("AUTH_4012", 401, "유효하지 않은 토큰입니다."),
    AUTH_4191("AUTH_4191", 419, "토큰이 만료되었습니다."),
    AUTH_4031("AUTH_4031", 403, "접근 권한이 없습니다."),

    // 요청/파라미터 오류 (REQ_XXXX)
    REQ_4001("REQ_4001", 400, "필수 파라미터가 누락되었습니다"),
    REQ_4002("REQ_4002", 400, "파라미터 형식이 잘못되었습니다"),
    REQ_4151("REQ_4151", 415, "지원하지 않는 Content-Type입니다"),

    // API/라우팅 (API_XXXX)
    API_4041("API_4041", 404, "존재하지 않는 API입니다"),
    API_4051("API_4051", 405, "지원하지 않는 HTTP 메서드입니다"),

    // 내부 서버 오류 (SERVER_XXXX)
    SERVER_5001("SERVER_5001", 500, "서버 내부 오류입니다."),
    SERVER_5041("SERVER_5041", 504, "외부 서비스 응답 지연"),
    SERVER_5031("SERVER_5031", 503, "서버가 일시적으로 불안정합니다");

    private final String code;
    private final int status;
    private final String message;

    ErrorCode(String code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}

