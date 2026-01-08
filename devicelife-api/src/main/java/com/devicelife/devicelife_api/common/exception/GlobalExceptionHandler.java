package com.devicelife.devicelife_api.common.exception;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("CustomException: code={}, message={}", errorCode.getCode(), errorCode.getMessage(), ex);

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode.getCode(), errorCode.getMessage(), null));
    }

    /**
     * Validation 실패 (@Valid, @Validated)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorDetail = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("Validation Exception: {}", errorDetail, ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(
                        ErrorCode.REQ_4002.getCode(),
                        ErrorCode.REQ_4002.getMessage(),
                        errorDetail
                ));
    }

    /**
     * 필수 파라미터 누락
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        log.error("Missing Parameter: {}", ex.getParameterName(), ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(
                        ErrorCode.REQ_4001.getCode(),
                        ErrorCode.REQ_4001.getMessage(),
                        "누락된 파라미터: " + ex.getParameterName()
                ));
    }

    /**
     * 파라미터 타입 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type Mismatch: {}", ex.getName(), ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(
                        ErrorCode.REQ_4002.getCode(),
                        ErrorCode.REQ_4002.getMessage(),
                        String.format("파라미터 '%s'의 타입이 올바르지 않습니다", ex.getName())
                ));
    }

    /**
     * HTTP 메시지 읽기 실패 (JSON 파싱 오류 등)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("Message Not Readable Exception", ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(
                        ErrorCode.REQ_4002.getCode(),
                        ErrorCode.REQ_4002.getMessage(),
                        "요청 본문을 읽을 수 없습니다"
                ));
    }

    /**
     * 지원하지 않는 Content-Type
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        log.error("Media Type Not Supported: {}", ex.getContentType(), ex);

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ApiResponse.fail(
                        ErrorCode.REQ_4151.getCode(),
                        ErrorCode.REQ_4151.getMessage(),
                        "지원되는 Content-Type: " + ex.getSupportedMediaTypes()
                ));
    }

    /**
     * 존재하지 않는 API 엔드포인트
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFound(NoHandlerFoundException ex) {
        log.error("No Handler Found: {} {}", ex.getHttpMethod(), ex.getRequestURL(), ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(
                        ErrorCode.API_4041.getCode(),
                        ErrorCode.API_4041.getMessage(),
                        ex.getRequestURL()
                ));
    }

    /**
     * 지원하지 않는 HTTP 메서드
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.error("Method Not Supported: {}", ex.getMethod(), ex);

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.fail(
                        ErrorCode.API_4051.getCode(),
                        ErrorCode.API_4051.getMessage(),
                        "지원되는 메서드: " + ex.getSupportedHttpMethods()
                ));
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        log.error("Unhandled Exception", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(
                        ErrorCode.SERVER_5001.getCode(),
                        ErrorCode.SERVER_5001.getMessage(),
                        ex.getMessage()
                ));
    }
}

