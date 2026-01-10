package com.devicelife.devicelife_api.controller.lifestyle;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.domain.lifestyle.response.LifestyleFeaturedResponse;
import com.devicelife.devicelife_api.service.lifestyle.LifestyleFeaturedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Lifestyle", description = "라이프스타일 화면 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lifestyle")
public class LifestyleFeaturedController {

    private final LifestyleFeaturedService service;

    @Operation(
            summary = "라이프스타일 태그별 대표 추천 조합 조회",
            description = "tagKey에 해당하는 대표 추천 기기 3개를 반환합니다. (이미지/이름/출시일/가격)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = LifestyleFeaturedResponse.class))
    )
    @GetMapping("/featured")
    public ApiResponse<LifestyleFeaturedResponse> getFeatured(@RequestParam String tagKey) {
        var result = service.getFeaturedByTagKey(tagKey);
        return ApiResponse.success("LIFESTYLE_2001", "라이프스타일 대표 조합 조회에 성공했습니다.", result);
    }
}
