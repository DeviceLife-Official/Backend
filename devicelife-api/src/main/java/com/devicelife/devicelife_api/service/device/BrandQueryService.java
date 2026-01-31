package com.devicelife.devicelife_api.service.device;

import com.devicelife.devicelife_api.domain.device.dto.response.BrandResponseDto;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import com.devicelife.devicelife_api.repository.device.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 브랜드 조회 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandQueryService {

    private final BrandRepository brandRepository;

    public List<BrandResponseDto> getAllBrands(DeviceType deviceType) {

        if (deviceType == null) {
            return brandRepository.findAll().stream()
                    .map(BrandResponseDto::from)
                    .toList();
        } else {
            return brandRepository.findByDeviceType(deviceType).stream()
                    .map(BrandResponseDto::from)
                    .toList();
        }
    }
}
