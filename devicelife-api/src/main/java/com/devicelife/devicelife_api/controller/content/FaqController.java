package com.devicelife.devicelife_api.controller.content;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.domain.content.dto.req.FaqSaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.FaqResponseDto;
import com.devicelife.devicelife_api.service.content.command.FaqCommandService;
import com.devicelife.devicelife_api.service.content.query.FaqQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class FaqController implements FaqControllerDocs {

    private final FaqCommandService faqCommandService;
    private final FaqQueryService faqQueryService;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @PostMapping
    public ApiResponse<FaqResponseDto> createFaq(
            @Valid @RequestBody FaqSaveRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.FAQ_CREATE_SUCCESS.getCode(),
                SuccessCode.FAQ_CREATE_SUCCESS.getMessage(),
                faqCommandService.createFaq(request)
        );
    }

    @Override
    @GetMapping
    public ApiResponse<List<FaqResponseDto>> getFaqs(
            @RequestParam(required = false, defaultValue = "true") Boolean isPublished
    ) {
        return ApiResponse.success(
                SuccessCode.FAQ_GET_SUCCESS.getCode(),
                SuccessCode.FAQ_GET_SUCCESS.getMessage(),
                faqQueryService.getFaqs(isPublished)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @PutMapping("/{faqId}")
    public ApiResponse<FaqResponseDto> updateFaq(
            @PathVariable @Positive Long faqId,
            @Valid @RequestBody FaqSaveRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.FAQ_UPDATE_SUCCESS.getCode(),
                SuccessCode.FAQ_UPDATE_SUCCESS.getMessage(),
                faqCommandService.updateFaq(faqId, request)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @DeleteMapping("/{faqId}")
    public ApiResponse<Void> deleteFaq(
            @PathVariable @Positive Long faqId
    ) {
        faqCommandService.deleteFaq(faqId);
        return ApiResponse.success(
                SuccessCode.FAQ_DELETE_SUCCESS.getCode(),
                SuccessCode.FAQ_DELETE_SUCCESS.getMessage(),
                null
        );
    }
}
