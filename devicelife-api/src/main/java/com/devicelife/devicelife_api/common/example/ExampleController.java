package com.devicelife.devicelife_api.common.example;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * ApiResponse 사용 예시 컨트롤러
 * 실제 프로젝트에서는 삭제하거나 주석 처리하세요.
 */
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    /**
     * 성공 응답 예시
     */
    @GetMapping("/success")
    public ApiResponse<ExampleDto> successExample() {
        ExampleDto data = new ExampleDto(123L, "샘플 데이터", "설명입니다.");

        return ApiResponse.success(
                SuccessCode.USER_2003.getCode(),
                SuccessCode.USER_2003.getMessage(),
                data
        );
    }

    /**
     * CustomException 발생 예시
     */
    @GetMapping("/error")
    public ApiResponse<Object> errorExample() {
        // CustomException을 발생시키면 GlobalExceptionHandler에서 처리됨
        throw new CustomException(ErrorCode.AUTH_4011);
    }

    /**
     * 파라미터 검증 실패 예시
     */
    @GetMapping("/validation-error")
    public ApiResponse<Object> validationErrorExample(@RequestParam String requiredParam) {
        // requiredParam을 전달하지 않으면 MissingServletRequestParameterException 발생
        return ApiResponse.success("EXAMPLE_2001", "파라미터 검증 성공", requiredParam);
    }

    /**
     * 예시 DTO
     */
    @Getter
    @RequiredArgsConstructor
    static class ExampleDto {
        private final Long id;
        private final String title;
        private final String description;
    }
}

