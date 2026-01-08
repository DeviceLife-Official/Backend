package com.devicelife.devicelife_api.domain.device;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagId")
    private Long tagId;

    @Column(name = "tagKey", length = 50, nullable = false, unique = true)
    private String tagKey;

    @Column(name = "tagLabel", length = 80, nullable = false)
    private String tagLabel;

    @Column(name = "tagType", length = 30, nullable = false)
    private String tagType;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

