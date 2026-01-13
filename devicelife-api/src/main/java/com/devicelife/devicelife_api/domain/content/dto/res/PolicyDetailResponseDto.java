package com.devicelife.devicelife_api.domain.content.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PolicyDetailResponseDto {

    private Long policyId;
    private String policyType;
    private String version;
    private String body;
    private LocalDateTime publishedAt;
    private Boolean isActive;

    public static PolicyDetailResponseDto from(com.devicelife.devicelife_api.domain.content.Policy policy) {
        return PolicyDetailResponseDto.builder()
                .policyId(policy.getPolicyId())
                .policyType(policy.getPolicyType())
                .version(policy.getVersion())
                .body(policy.getBody())
                .publishedAt(policy.getPublishedAt())
                .isActive(policy.isActive())
                .build();
    }
}
