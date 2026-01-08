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
}

