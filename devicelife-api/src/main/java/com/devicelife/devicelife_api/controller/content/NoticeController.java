package com.devicelife.devicelife_api.controller.content;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.domain.content.dto.req.NoticeSaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.NoticeDetailResponseDto;
import com.devicelife.devicelife_api.domain.content.dto.res.NoticeResponseDto;
import com.devicelife.devicelife_api.service.content.command.NoticeCommandService;
import com.devicelife.devicelife_api.service.content.query.NoticeQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController implements NoticeControllerDocs {

    private final NoticeQueryService noticeQueryService;
    private final NoticeCommandService noticeCommandService;

    @Override
    @GetMapping
    public ApiResponse<Page<NoticeResponseDto>> getNotices(
            @RequestParam(required = false, defaultValue = "true") Boolean isPublished,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ApiResponse.success(
                SuccessCode.NOTICE_LIST_SUCCESS.getCode(),
                SuccessCode.NOTICE_LIST_SUCCESS.getMessage(),
                noticeQueryService.getNotices(isPublished, pageable)
        );
    }

    @Override
    @GetMapping("/{noticeId}")
    public ApiResponse<NoticeDetailResponseDto> getNotice(
            @PathVariable @Positive Long noticeId
    ) {
        return ApiResponse.success(
                SuccessCode.NOTICE_GET_SUCCESS.getCode(),
                SuccessCode.NOTICE_GET_SUCCESS.getMessage(),
                noticeQueryService.getNoticeById(noticeId)
        );
    }

    @Override
    @PostMapping
    public ApiResponse<NoticeDetailResponseDto> createNotice(
            @Valid @RequestBody NoticeSaveRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.NOTICE_CREATE_SUCCESS.getCode(),
                SuccessCode.NOTICE_CREATE_SUCCESS.getMessage(),
                noticeCommandService.createNotice(request)
        );
    }

    @Override
    @PutMapping("/{noticeId}")
    public ApiResponse<NoticeDetailResponseDto> updateNotice(
            @PathVariable @Positive Long noticeId,
            @Valid @RequestBody NoticeSaveRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.NOTICE_UPDATE_SUCCESS.getCode(),
                SuccessCode.NOTICE_UPDATE_SUCCESS.getMessage(),
                noticeCommandService.updateNotice(noticeId, request)
        );
    }

    @Override
    @DeleteMapping("/{noticeId}")
    public ApiResponse<Void> deleteNotice(
            @PathVariable @Positive Long noticeId
    ) {
        noticeCommandService.deleteNotice(noticeId);
        return ApiResponse.success(
                SuccessCode.NOTICE_DELETE_SUCCESS.getCode(),
                SuccessCode.NOTICE_DELETE_SUCCESS.getMessage(),
                null
        );
    }
}
