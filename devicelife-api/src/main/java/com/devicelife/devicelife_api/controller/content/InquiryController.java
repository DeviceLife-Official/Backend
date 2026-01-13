package com.devicelife.devicelife_api.controller.content;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.domain.content.InquiryStatus;
import com.devicelife.devicelife_api.domain.content.dto.req.InquirySaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.req.InquiryStatusUpdateDto;
import com.devicelife.devicelife_api.domain.content.dto.res.InquiryListDto;
import com.devicelife.devicelife_api.domain.content.dto.res.InquiryDetailResponseDto;
import com.devicelife.devicelife_api.service.content.command.InquiryCommandService;
import com.devicelife.devicelife_api.service.content.query.InquiryQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiryController implements InquiryControllerDocs {

    private final InquiryCommandService inquiryCommandService;
    private final InquiryQueryService inquiryQueryService;

    // TODO: JWT에서 userId 추출 로직으로 변환
    @Override
    @PostMapping("/{userId}")
    public ApiResponse<InquiryDetailResponseDto> createInquiry(
            @PathVariable @Positive Long userId,
            @Valid @RequestBody InquirySaveRequestDto request
            ) {
        return ApiResponse.success(
                SuccessCode.INQUIRY_CREATE_SUCCESS.getCode(),
                SuccessCode.INQUIRY_CREATE_SUCCESS.getMessage(),
                inquiryCommandService.createInquiry(userId, request)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @PutMapping("/{inquiryId}")
    public ApiResponse<InquiryDetailResponseDto> updateInquiryStatus(
            @PathVariable @Positive Long inquiryId,
            @Valid @RequestBody InquiryStatusUpdateDto request
    ) {
        return ApiResponse.success(
                SuccessCode.INQUIRY_STATUS_CHANGE_SUCCESS.getCode(),
                SuccessCode.INQUIRY_STATUS_CHANGE_SUCCESS.getMessage(),
                inquiryCommandService.updateInquiry(inquiryId, request)
        );
    }

    // TODO: JWT에서 userId 추출 로직으로 변환
    @Override
    @GetMapping("/my/{userId}")
    public ApiResponse<InquiryListDto> getMyInquiries(
            @PathVariable @Positive Long userId,
            @RequestParam(required = false) InquiryStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        return ApiResponse.success(
                SuccessCode.INQUIRY_LIST_SUCCESS.getCode(),
                SuccessCode.INQUIRY_LIST_SUCCESS.getMessage(),
                inquiryQueryService.getMyInquiries(userId, status, pageable)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @GetMapping("/admin")
    public ApiResponse<InquiryListDto> getAllInquiries(
            @RequestParam(required = false) InquiryStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(
                SuccessCode.INQUIRY_LIST_SUCCESS.getCode(),
                SuccessCode.INQUIRY_LIST_SUCCESS.getMessage(),
                inquiryQueryService.getAllInquiries(status, pageable)
        );
    }

    @Override
    @GetMapping("/{inquiryId}/{userId}")
    public ApiResponse<InquiryDetailResponseDto> getInquiryDetail(
            @PathVariable @Positive Long inquiryId,
            @PathVariable @Positive Long userId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return ApiResponse.success(
                SuccessCode.INQUIRY_GET_SUCCESS.getCode(),
                SuccessCode.INQUIRY_GET_SUCCESS.getMessage(),
                inquiryQueryService.getInquiryById(inquiryId, userId, isAdmin)
        );
    }
}
