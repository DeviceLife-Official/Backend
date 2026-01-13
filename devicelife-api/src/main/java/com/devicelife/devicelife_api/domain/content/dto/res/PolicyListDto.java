package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Policy;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PolicyListDto {

    private List<PolicyResponseDto> policies;
    private Integer listSize;
    private Integer totalPage;
    private Long totalElements;
    private Boolean isFirst;
    private Boolean isLast;

    public static PolicyListDto of(Page<Policy> policyPage) {
        List<PolicyResponseDto> policies = policyPage.getContent().stream()
                .map(PolicyResponseDto::from)
                .toList();

        return PolicyListDto.builder()
                .policies(policies)
                .listSize(policyPage.getNumberOfElements())
                .totalPage(policyPage.getTotalPages())
                .totalElements(policyPage.getTotalElements())
                .isFirst(policyPage.isFirst())
                .isLast(policyPage.isLast())
                .build();
    }
}
