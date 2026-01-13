package com.devicelife.devicelife_api.domain.content;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "policies")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policyId")
    private Long policyId;

    @Column(name = "policyType", length = 30, nullable = false)
    private String policyType;

    @Column(name = "version", length = 30, nullable = false)
    private String version;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    @Column(name = "isActive", nullable = false)
    @Builder.Default
    private boolean isActive = false;

    public void update(String policyType, String version, String body, boolean isActive) {
        this.policyType = policyType;
        this.version = version;
        this.body = body;

        // 활성화 여부가 false -> true로 변경될 때만 publishedAt 설정
        if (!this.isActive && isActive) {
            this.publishedAt = LocalDateTime.now();
        }

        // 활성화 여부가 true -> false로 변경될 때 publishedAt을 null로
        if (this.isActive && !isActive) {
            this.publishedAt = null;
        }

        this.isActive = isActive;
    }
}

