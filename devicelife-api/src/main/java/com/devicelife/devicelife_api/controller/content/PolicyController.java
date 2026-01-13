package com.devicelife.devicelife_api.controller.content;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.response.SuccessCode;
import com.devicelife.devicelife_api.domain.content.dto.req.PolicySaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.PolicyDetailResponseDto;
import com.devicelife.devicelife_api.domain.content.dto.res.PolicyListDto;
import com.devicelife.devicelife_api.service.content.command.PolicyCommandService;
import com.devicelife.devicelife_api.service.content.query.PolicyQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/policies")
public class PolicyController implements PolicyControllerDocs {

    private final PolicyCommandService policyCommandService;
    private final PolicyQueryService policyQueryService;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @PostMapping
    public ApiResponse<PolicyDetailResponseDto> createPolicy(
            @Valid @RequestBody PolicySaveRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.POLICY_CREATE_SUCCESS.getCode(),
                SuccessCode.POLICY_CREATE_SUCCESS.getMessage(),
                policyCommandService.createPolicy(request)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @PutMapping("/{policyId}")
    public ApiResponse<PolicyDetailResponseDto> updatePolicy(
            @PathVariable @Positive Long policyId,
            @Valid @RequestBody PolicySaveRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.POLICY_UPDATE_SUCCESS.getCode(),
                SuccessCode.POLICY_UPDATE_SUCCESS.getMessage(),
                policyCommandService.updatePolicy(policyId, request)
        );
    }

    @Override
    @GetMapping
    public ApiResponse<PolicyListDto> getPolicies(
            @RequestParam(required = false) String policyType,
            @RequestParam(required = false, defaultValue = "true") Boolean isActive,
            @PageableDefault(size = 20, sort = "version", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(
                SuccessCode.POLICY_LIST_SUCCESS.getCode(),
                SuccessCode.POLICY_LIST_SUCCESS.getMessage(),
                policyQueryService.getPolicies(policyType, isActive, pageable)
        );
    }

    @Override
    @GetMapping("/{policyId}")
    public ApiResponse<PolicyDetailResponseDto> getPolicyById(
            @PathVariable @Positive Long policyId
    ) {
        return ApiResponse.success(
                SuccessCode.POLICY_GET_SUCCESS.getCode(),
                SuccessCode.POLICY_GET_SUCCESS.getMessage(),
                policyQueryService.getPolicyById(policyId)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @DeleteMapping("/{policyId}")
    public ApiResponse<Void> deletePolicy(
            @PathVariable @Positive Long policyId
    ) {
        policyCommandService.deletePolicy(policyId);
        return ApiResponse.success(
                SuccessCode.POLICY_DELETE_SUCCESS.getCode(),
                SuccessCode.POLICY_DELETE_SUCCESS.getMessage(),
                null
        );
    }
}
