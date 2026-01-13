package com.devicelife.devicelife_api.service.content.command;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.content.Policy;
import com.devicelife.devicelife_api.domain.content.dto.req.PolicySaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.PolicyDetailResponseDto;
import com.devicelife.devicelife_api.repository.content.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyCommandService {

    private final PolicyRepository policyRepository;

    public PolicyDetailResponseDto createPolicy(PolicySaveRequestDto request) {
        boolean isActive = request.getIsActive() != null ? request.getIsActive() : false;

        Policy policy = Policy.builder()
                .policyType(request.getPolicyType())
                .version(request.getVersion())
                .body(request.getBody())
                .isActive(isActive)
                .publishedAt(isActive ? LocalDateTime.now() : null)
                .build();

        Policy savedPolicy = policyRepository.save(policy);
        return PolicyDetailResponseDto.from(savedPolicy);
    }

    public PolicyDetailResponseDto updatePolicy(Long policyId, PolicySaveRequestDto request) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new CustomException(ErrorCode.POLICY_4041));

        boolean isActive = request.getIsActive() != null ? request.getIsActive() : policy.isActive();
        policy.update(request.getPolicyType(), request.getVersion(), request.getBody(), isActive);

        return PolicyDetailResponseDto.from(policy);
    }

    public void deletePolicy(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new CustomException(ErrorCode.POLICY_4041));

        policyRepository.delete(policy);
    }
}
