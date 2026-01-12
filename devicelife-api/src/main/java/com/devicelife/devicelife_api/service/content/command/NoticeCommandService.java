package com.devicelife.devicelife_api.service.content.command;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.content.Notice;
import com.devicelife.devicelife_api.domain.content.dto.req.NoticeSaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.NoticeDetailResponseDto;
import com.devicelife.devicelife_api.repository.content.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandService {

    private final NoticeRepository noticeRepository;

    public NoticeDetailResponseDto createNotice(NoticeSaveRequestDto request) {
        boolean isPublished = request.getIsPublished() != null ? request.getIsPublished() : false;

        Notice notice = Notice.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .isPublished(isPublished)
                .publishedAt(isPublished ? LocalDateTime.now() : null)
                .build();

        Notice savedNotice = noticeRepository.save(notice);

        return NoticeDetailResponseDto.from(savedNotice);
    }

    public NoticeDetailResponseDto updateNotice(Long noticeId, NoticeSaveRequestDto request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_4041));

        boolean isPublished = request.getIsPublished() != null ? request.getIsPublished() : false;
        notice.update(request.getTitle(), request.getBody(), isPublished);

        return NoticeDetailResponseDto.from(notice);
    }

    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_4041));

        noticeRepository.delete(notice);
    }
}
