package com.devicelife.devicelife_api.service.content.query;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.content.Policy;
import com.devicelife.devicelife_api.domain.content.dto.res.PolicyDetailResponseDto;
import com.devicelife.devicelife_api.domain.content.dto.res.PolicyListDto;
import com.devicelife.devicelife_api.repository.content.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyQueryService {

    private final PolicyRepository policyRepository;

    public PolicyListDto getPolicies(String policyType, Boolean isActive, Pageable pageable) {
        Page<Policy> policies;

        if (policyType != null) {
            policies = policyRepository.findAllByPolicyTypeAndIsActive(policyType, isActive, pageable);
        } else {
            policies = policyRepository.findAllByIsActive(isActive, pageable);
        }
        return PolicyListDto.of(policies);
    }

    public PolicyDetailResponseDto getPolicyById(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new CustomException(ErrorCode.POLICY_4041));

        return PolicyDetailResponseDto.from(policy);
    }
}
