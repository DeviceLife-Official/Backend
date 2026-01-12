package com.devicelife.devicelife_api.repository.content;

import com.devicelife.devicelife_api.domain.content.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Page<Policy> findAllByPolicyTypeAndIsActive(String policyType, Boolean isActive, Pageable pageable);

    Page<Policy> findAllByIsActive(Boolean isActive, Pageable pageable);
}
