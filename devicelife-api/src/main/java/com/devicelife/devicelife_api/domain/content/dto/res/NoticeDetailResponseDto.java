package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeDetailResponseDto {

    private Long noticeId;
    private String title;
    private String body;
    private boolean isPublished;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NoticeDetailResponseDto from(Notice notice) {
        return NoticeDetailResponseDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .body(notice.getBody())
                .isPublished(notice.isPublished())
                .publishedAt(notice.getPublishedAt())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
