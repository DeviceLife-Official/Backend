package com.devicelife.devicelife_api.service.content.query;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.content.Notice;
import com.devicelife.devicelife_api.domain.content.dto.res.NoticeDetailResponseDto;
import com.devicelife.devicelife_api.domain.content.dto.res.NoticeListDto;
import com.devicelife.devicelife_api.domain.content.dto.res.NoticeResponseDto;
import com.devicelife.devicelife_api.repository.content.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryService {

    private final NoticeRepository noticeRepository;

    public NoticeListDto getNotices(Boolean isPublished, Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAllByIsPublished(isPublished, pageable);
        return NoticeListDto.of(notices);
    }

    public NoticeDetailResponseDto getNoticeById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_4041));

        return NoticeDetailResponseDto.from(notice);
    }
}
