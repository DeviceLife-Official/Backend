package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Policy;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PolicyResponseDto {

    private Long policyId;
    private String policyType;
    private String version;
    private LocalDateTime publishedAt;
    private Boolean isActive;

    public static PolicyResponseDto from(Policy policy) {
        return PolicyResponseDto.builder()
                .policyId(policy.getPolicyId())
                .policyType(policy.getPolicyType())
                .version(policy.getVersion())
                .publishedAt(policy.getPublishedAt())
                .isActive(policy.isActive())
                .build();
    }
}
