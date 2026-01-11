package com.devicelife.devicelife_api.service.combo;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.combo.Combo;
import com.devicelife.devicelife_api.domain.combo.ComboDevice;
import com.devicelife.devicelife_api.domain.combo.ComboDeviceId;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboCreateRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboDeviceAddRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboUpdateRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.response.*;
import com.devicelife.devicelife_api.domain.device.Device;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.repository.combo.ComboDeviceRepository;
import com.devicelife.devicelife_api.repository.combo.ComboRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComboService {

    private final ComboRepository comboRepository;
    private final ComboDeviceRepository comboDeviceRepository;
    private final EntityManager em;

    /**
     * 조합 생성
     */
    @Transactional
    public ComboCreateResponseDto createCombo(ComboCreateRequestDto request) {
        Long userId = request.getUserId();

        User user = em.find(User.class, userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_4041);
        }

        Combo combo = Combo.builder()
                .user(user)
                .comboName(request.getComboName())
                .build();

        Combo saved = comboRepository.save(combo);

        return ComboCreateResponseDto.builder()
                .comboId(saved.getComboId())
                .comboName(saved.getComboName())
                .build();
    }

    /**
     * 조합 상세 조회
     */
    @Transactional(readOnly = true)
    public ComboDetailResponseDto getComboDetail(Long comboId) {
        Combo combo = comboRepository.findActiveComboById(comboId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMBO_4041));

        List<ComboDevice> comboDevices = comboDeviceRepository.findAllByComboId(comboId);

        List<ComboDeviceResponseDto> deviceDtos = comboDevices.stream()
                .map(cd -> ComboDeviceResponseDto.builder()
                        .deviceId(cd.getDevice().getDeviceId())
                        .modelName(cd.getDevice().getModelName())
                        .brandName(cd.getDevice().getBrand().getBrandName())
                        .categoryName(cd.getDevice().getCategory().getCategoryName())
                        .price(cd.getDevice().getMsrp())
                        .addedAt(cd.getAddedAt())
                        .build())
                .collect(Collectors.toList());

        return ComboDetailResponseDto.builder()
                .comboId(combo.getComboId())
                .comboName(combo.getComboName())
                .isPinned(combo.isPinned())
                .pinnedAt(combo.getPinnedAt())
                .totalPrice(combo.getTotalPrice())
                .currentTotalScore(combo.getCurrentTotalScore())
                .evaluatedAt(combo.getEvaluatedAt())
                .createdAt(combo.getCreatedAt())
                .updatedAt(combo.getUpdatedAt())
                .devices(deviceDtos)
                .build();
    }

    /**
     * 조합 목록 조회 (활성 조합만)
     */
    @Transactional(readOnly = true)
    public List<ComboListResponseDto> getComboList(Long userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_4041);
        }

        List<Combo> combos = comboRepository.findActiveCombosByUserId(userId);

        return combos.stream()
                .map(combo -> {
                    Long deviceCount = comboDeviceRepository.countByComboId(combo.getComboId());
                    return ComboListResponseDto.builder()
                            .comboId(combo.getComboId())
                            .comboName(combo.getComboName())
                            .isPinned(combo.isPinned())
                            .pinnedAt(combo.getPinnedAt())
                            .totalPrice(combo.getTotalPrice())
                            .currentTotalScore(combo.getCurrentTotalScore())
                            .deviceCount(deviceCount.intValue())
                            .createdAt(combo.getCreatedAt())
                            .updatedAt(combo.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 조합 수정 (조합명 변경)
     */
    @Transactional
    public ComboDetailResponseDto updateCombo(Long comboId, ComboUpdateRequestDto request) {
        Combo combo = comboRepository.findActiveComboById(comboId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMBO_4041));

        combo.updateComboName(request.getComboName());

        return getComboDetail(comboId);
    }

    /**
     * 조합 삭제 (소프트 삭제 - 휴지통으로 이동)
     */
    @Transactional
    public void deleteCombo(Long comboId) {
        Combo combo = comboRepository.findActiveComboById(comboId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMBO_4041));

        combo.softDelete();
    }

    /**
     * 조합 즐겨찾기 토글
     */
    @Transactional
    public ComboDetailResponseDto togglePin(Long comboId) {
        Combo combo = comboRepository.findActiveComboById(comboId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMBO_4041));

        combo.togglePin();

        return getComboDetail(comboId);
    }

    /**
     * 조합에 기기 추가
     */
    @Transactional
    public ComboDetailResponseDto addDeviceToCombo(Long comboId, ComboDeviceAddRequestDto request) {
        Combo combo = comboRepository.findActiveComboById(comboId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMBO_4041));

        Device device = em.find(Device.class, request.getDeviceId());
        if (device == null) {
            throw new CustomException(ErrorCode.DEVICE_4041);
        }

        ComboDeviceId comboDeviceId = new ComboDeviceId(comboId, request.getDeviceId());
        if (comboDeviceRepository.existsById(comboDeviceId)) {
            throw new CustomException(ErrorCode.COMBO_4003);
        }

        ComboDevice comboDevice = ComboDevice.builder()
                .id(comboDeviceId)
                .combo(combo)
                .device(device)
                .build();

        comboDeviceRepository.save(comboDevice);

        // 총 가격 업데이트
        updateComboTotalPrice(combo);

        return getComboDetail(comboId);
    }

    /**
     * 조합에서 기기 삭제
     */
    @Transactional
    public ComboDetailResponseDto removeDeviceFromCombo(Long comboId, Long deviceId) {
        Combo combo = comboRepository.findActiveComboById(comboId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMBO_4041));

        ComboDeviceId comboDeviceId = new ComboDeviceId(comboId, deviceId);
        if (!comboDeviceRepository.existsById(comboDeviceId)) {
            throw new CustomException(ErrorCode.COMBO_4004);
        }

        comboDeviceRepository.deleteById(comboDeviceId);

        // 총 가격 업데이트
        updateComboTotalPrice(combo);

        return getComboDetail(comboId);
    }

    /**
     * 휴지통 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ComboTrashResponseDto> getTrashList(Long userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_4041);
        }

        List<Combo> deletedCombos = comboRepository.findDeletedCombosByUserId(userId);

        return deletedCombos.stream()
                .map(combo -> {
                    Long deviceCount = comboDeviceRepository.countByComboId(combo.getComboId());
                    long daysUntilDelete = calculateDaysUntilPermanentDelete(combo.getDeletedAt());

                    return ComboTrashResponseDto.builder()
                            .comboId(combo.getComboId())
                            .comboName(combo.getComboName())
                            .totalPrice(combo.getTotalPrice())
                            .deviceCount(deviceCount.intValue())
                            .deletedAt(combo.getDeletedAt())
                            .daysUntilPermanentDelete(daysUntilDelete)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 휴지통에서 복구
     */
    @Transactional
    public ComboDetailResponseDto restoreCombo(Long comboId) {
        Combo combo = comboRepository.findById(comboId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMBO_4041));

        if (!combo.isDeleted()) {
            throw new CustomException(ErrorCode.COMBO_4041);
        }

        combo.restore();

        return getComboDetail(comboId);
    }

    /**
     * 휴지통에서 영구 삭제
     */
    @Transactional
    public void permanentDeleteCombos(List<Long> comboIds) {
        for (Long comboId : comboIds) {
            Combo combo = comboRepository.findById(comboId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COMBO_4041));

            if (!combo.isDeleted()) {
                throw new CustomException(ErrorCode.COMBO_4002);
            }

            // 조합에 연결된 모든 기기 삭제
            comboDeviceRepository.deleteAllByIdComboId(comboId);

            // 조합 영구 삭제
            comboRepository.delete(combo);
        }
    }

    /**
     * 30일 지난 조합 자동 삭제 (스케줄러에서 호출)
     */
    @Transactional
    public void autoDeleteExpiredCombos() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        List<Combo> expiredCombos = comboRepository.findCombosToAutoDelete(threshold);

        for (Combo combo : expiredCombos) {
            comboDeviceRepository.deleteAllByIdComboId(combo.getComboId());
            comboRepository.delete(combo);
        }
    }

    /**
     * 조합의 총 가격 업데이트
     */
    private void updateComboTotalPrice(Combo combo) {
        List<ComboDevice> comboDevices = comboDeviceRepository.findAllByComboId(combo.getComboId());
        Integer totalPrice = comboDevices.stream()
                .map(cd -> cd.getDevice().getMsrp() != null ? cd.getDevice().getMsrp() : 0)
                .reduce(0, Integer::sum);

        combo.updateTotalPrice(totalPrice);
    }

    /**
     * 영구 삭제까지 남은 일수 계산
     */
    private long calculateDaysUntilPermanentDelete(LocalDateTime deletedAt) {
        LocalDateTime permanentDeleteDate = deletedAt.plusDays(30);
        return ChronoUnit.DAYS.between(LocalDateTime.now(), permanentDeleteDate);
    }
}
