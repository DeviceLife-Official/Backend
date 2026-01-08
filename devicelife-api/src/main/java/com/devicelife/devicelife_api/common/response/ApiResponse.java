package com.devicelife.devicelife_api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean isSuccess;
    private final String code;
    private final String message;
    private final T result;
    private final Object error;

    private ApiResponse(boolean isSuccess, String code, String message, T result, Object error) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
        this.error = error;
    }

    /**
     * 성공 응답 생성
     *
     * @param code    응답 코드 (예: "USER_2001")
     * @param message 사용자에게 보여줄 메시지
     * @param result  응답 데이터 (DTO, List 등)
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> success(String code, String message, T result) {
        return new ApiResponse<>(true, code, message, result, null);
    }

    /**
     * 실패 응답 생성
     *
     * @param code        에러 코드 (예: "AUTH_4011")
     * @param message     사용자에게 보여줄 메시지
     * @param errorDetail 오류 상세 정보 (선택적)
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> fail(String code, String message, Object errorDetail) {
        return new ApiResponse<>(false, code, message, null, errorDetail);
    }
}

