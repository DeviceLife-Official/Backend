package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponseDto {

    private Long noticeId;
    private String title;
    private boolean isPublished;
    private LocalDateTime publishedAt;

    public static NoticeResponseDto from(Notice notice) {
        return NoticeResponseDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .isPublished(notice.isPublished())
                .publishedAt(notice.getPublishedAt())
                .build();
    }
}
