package com.devicelife.devicelife_api.service.recentview;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.common.util.CurrencyConverter;
import com.devicelife.devicelife_api.domain.device.Device;
import com.devicelife.devicelife_api.domain.recentview.RecentlyViewedDevice;
import com.devicelife.devicelife_api.domain.recentview.dto.response.RecentlyViewedDeviceResponseDto;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.repository.recentview.RecentlyViewedDeviceRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecentlyViewedDeviceService {

    private final RecentlyViewedDeviceRepository recentlyViewedDeviceRepository;
    private final EntityManager em;
    private final CurrencyConverter currencyConverter;

    /**
     * 최근 본 기기 목록 조회 (최신순, 상위 2개)
     */
    @Transactional(readOnly = true)
    public List<RecentlyViewedDeviceResponseDto> getRecentlyViewedDevices(CustomUserDetails cud) {
        Long userId = cud.getId();

        User user = em.find(User.class, userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_4041);
        }

        List<RecentlyViewedDevice> recentlyViewedDevices = 
                recentlyViewedDeviceRepository.findTopNByUserIdOrderByViewedAtDesc(userId, 2);

        return recentlyViewedDevices.stream()
                .map(rv -> {
                    Device device = rv.getDevice();
                    Integer priceKrw = currencyConverter.convertToKRW(device.getPrice(), device.getPriceCurrency());

                    return RecentlyViewedDeviceResponseDto.builder()
                            .deviceId(device.getDeviceId())
                            .name(device.getName())
                            .modelCode(device.getModelCode())
                            .brandName(device.getBrand().getBrandName())
                            .deviceType(device.getDeviceType() != null 
                                    ? device.getDeviceType().getDisplayName() : null)
                            .price(device.getPrice())
                            .priceCurrency(device.getPriceCurrency())
                            .priceKrw(priceKrw)
                            .imageUrl(device.getImageUrl())
                            .viewedAt(rv.getViewedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 기기 조회 기록 저장 (또는 업데이트)
     * 이미 조회한 기기는 조회 시간만 업데이트
     */
    @Transactional
    public void recordDeviceView(Long userId, Long deviceId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_4041);
        }

        Device device = em.find(Device.class, deviceId);
        if (device == null) {
            throw new CustomException(ErrorCode.DEVICE_4041);
        }

        Optional<RecentlyViewedDevice> existing = 
                recentlyViewedDeviceRepository.findByUserIdAndDeviceId(userId, deviceId);

        if (existing.isPresent()) {
            // 이미 조회한 기기인 경우 조회 시간만 업데이트
            existing.get().updateViewedAt();
        } else {
            // 새로운 기기 조회 기록 저장
            RecentlyViewedDevice recentlyViewedDevice = RecentlyViewedDevice.builder()
                    .user(user)
                    .device(device)
                    .build();

            recentlyViewedDeviceRepository.save(recentlyViewedDevice);
        }
    }
}
