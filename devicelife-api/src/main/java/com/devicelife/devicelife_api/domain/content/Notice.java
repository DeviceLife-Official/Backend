package com.devicelife.devicelife_api.domain.content;

import com.devicelife.devicelife_api.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noticeId")
    private Long noticeId;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "isPublished", nullable = false)
    @Builder.Default
    private boolean isPublished = false;

    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    public void update(String title, String body, boolean isPublished) {
        this.title = title;
        this.body = body;

        // 게시 상태가 false -> true로 변경될 때만 publishedAt 설정
        if (!this.isPublished && isPublished) {
            this.publishedAt = LocalDateTime.now();
        }

        // 게시 상태가 true -> false로 변경될 때 publishedAt을 null로
        if (this.isPublished && !isPublished) {
            this.publishedAt = null;
        }

        this.isPublished = isPublished;
    }
}

