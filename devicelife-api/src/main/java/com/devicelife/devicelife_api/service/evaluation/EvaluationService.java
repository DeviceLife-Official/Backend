package com.devicelife.devicelife_api.service.evaluation;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.domain.combo.Combo;
import com.devicelife.devicelife_api.domain.combo.ComboDevice;
import com.devicelife.devicelife_api.domain.device.*;
import com.devicelife.devicelife_api.domain.evaluation.Evaluation;
import com.devicelife.devicelife_api.domain.evaluation.dto.EvaluationDto;
import com.devicelife.devicelife_api.domain.evaluation.dto.EvaluationPayloadResDto;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.repository.device.*;
import com.devicelife.devicelife_api.repository.evaluation.EvaluationRepository;
import com.devicelife.devicelife_api.repository.combo.ComboRepository;
import com.devicelife.devicelife_api.repository.tag.UserTagRepository;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.devicelife.devicelife_api.common.exception.ErrorCode.COMBO_4041;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationService {

    private final EntityManager em;
    private final ComboRepository comboRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserTagRepository userTagRepository;

    private final SmartphoneRepository smartphoneRepository;
    private final LaptopRepository laptopRepository;
    private final TabletRepository tabletRepository;
    private final KeyboardRepository keyboardRepository;
    private final MouseRepository mouseRepository;
    private final ChargerRepository chargerRepository;
    private final SmartwatchRepository smartwatchRepository;
    private final AudioRepository audioRepository;
    private final UserRepository userRepository;

    public EvaluationPayloadResDto getDevicesData (Long comboId) {


        Combo combo = comboRepository.findByComboId(comboId)
                .orElseThrow(() -> new CustomException(COMBO_4041));

        User user = combo.getUser();

        List<String> lifestyle = userTagRepository.findTagLabelsByUserIdOrderByTagLabelAsc(user.getUserId());

        List<EvaluationPayloadResDto.DeviceDto> devices = combo.getComboDevices().stream()
                .map(ComboDevice::getDevice)
                .map(this::toDeviceDto)
                .toList();

        return new EvaluationPayloadResDto(
                combo.getComboId(),
                combo.getEvaluationVersion(),
                null,                      // jobId (없으면 null)
                devices,
                lifestyle
        );
    }

    public void postEvaluation (Long comboId,EvaluationDto.postEvaluationReqDto req) {

        Combo combo = comboRepository.findByComboId(comboId)
                .orElseThrow(() -> new CustomException(COMBO_4041));

        Evaluation current = combo.getCurrentEvaluation();
        Long currentVersion = (current == null) ? 0L : current.getVersion();

        // 구버전 결과면 무시 (워커가 성공처리하고 메시지 delete 하게)
        if (req.getEvaluationVersion() < currentVersion) {
            return;
        }
        // 같은 버전이면 그냥 OK
        if (req.getEvaluationVersion().equals(currentVersion) && current != null) {
            return;
        }

        // 3) 새 Evaluation 생성/저장
        evaluationRepository.deleteAllByComboId(comboId);
        em.flush();   // ✅ 여기서 한번 더 확실히 DB flush
        em.clear();   // ✅ 프록시/영속성 꼬임 방지 (선택이지만 추천)

        Evaluation newEval = Evaluation.builder()
                .combo(combo)
                .version(req.getEvaluationVersion())
                .totalScore(req.getTotalScore())
                .scoreA(req.getCompatibilityScore())
                .scoreB(req.getConvenienceScore())
                .scoreC(req.getLifestyleScore())
                .reportJson(null) // 추후 확장
                .build();

        newEval = evaluationRepository.save(newEval);

        // combo가 새 evaluation을 가리키게 갱신
        // Long oldEvaluationId = (current == null) ? null : current.getEvaluationId();
        combo.applyEvaluation(newEval);

        // 이전 평가 삭제 (combo가 더 이상 참조하지 않는 상태)
        //if (oldEvaluationId != null) {
        //    evaluationRepository.deleteById(oldEvaluationId);
        //}

    }


    private EvaluationPayloadResDto.DeviceDto toDeviceDto(Device device) {

        EvaluationPayloadResDto.DeviceType type = toDeviceType(device);
        EvaluationPayloadResDto.Specs specs = toSpecs(type, device);

        return new EvaluationPayloadResDto.DeviceDto(
                device.getDeviceId(),
                type,
                specs
        );
    }

    private EvaluationPayloadResDto.DeviceType toDeviceType(Device device) {

        return EvaluationPayloadResDto.DeviceType.valueOf(device.getDeviceType().name());
    }

    private EvaluationPayloadResDto.Specs toSpecs(EvaluationPayloadResDto.DeviceType type, Device device) {

        Long deviceId = device.getDeviceId();

        return switch (type) {
            case SMARTPHONE -> toSmartphoneSpecs(deviceId);
            case LAPTOP -> toLaptopSpecs(deviceId);
            case TABLET -> toTabletSpecs(deviceId);
            case SMARTWATCH -> toSmartwatchSpecs(deviceId);
            case AUDIO -> toAudioSpecs(deviceId);
            case KEYBOARD -> toKeyboardSpecs(deviceId);
            case MOUSE -> toMouseSpecs(deviceId);
            case CHARGER -> toChargerSpecs(deviceId);
        };
    }

    /* =========================
     * 타입별 Specs 매핑 (엔티티 -> DTO)
     * ========================= */

    /* =========================
     * SMARTPHONE
     * ========================= */
    private EvaluationPayloadResDto.SmartphoneSpecs toSmartphoneSpecs(Long deviceId) {
        Smartphone d = smartphoneRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("Smartphone not found. deviceId=" + deviceId));

        return new EvaluationPayloadResDto.SmartphoneSpecs(
                d.getOs().name(),
                d.getChargingPort().name(),
                d.getWirelessCharging() == null ? null : d.getWirelessCharging().name(),
                d.getMaxInputPowerW(),
                d.getBiometricType() == null ? null : d.getBiometricType().name(),
                d.getStorageGb(),
                d.getRamGb(),
                d.getScreenInch(),
                d.getBatteryMah()
        );
    }

    /* =========================
     * LAPTOP
     * ========================= */
    private EvaluationPayloadResDto.LaptopSpecs toLaptopSpecs(Long deviceId) {
        Laptop d = laptopRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("Laptop not found. deviceId=" + deviceId));

        return new EvaluationPayloadResDto.LaptopSpecs(
                d.getOs().name(),
                d.getCpu(),
                d.getGpu(),
                d.getMinRequiredPowerW(),
                d.getChargingMethod().name(),
                d.getHasHdmi(),
                d.getHasThunderbolt(),
                d.getHasUsbA(),
                d.getUsbCPortCount(),
                d.getWeightKg(),
                d.getRamGb(),
                d.getStorageGb(),
                d.getScreenInch(),
                d.getBatteryWh()
        );
    }

    /* =========================
     * TABLET
     * ========================= */
    private EvaluationPayloadResDto.TabletSpecs toTabletSpecs(Long deviceId) {
        Tablet d = tabletRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("Tablet not found. deviceId=" + deviceId));

        return new EvaluationPayloadResDto.TabletSpecs(
                d.getOs().name(),
                d.getScreenInch(),
                d.getStylusType() == null ? null : d.getStylusType().name(),
                d.getKeyboardConnection() == null ? null : d.getKeyboardConnection().name(),
                d.getChargingPort().name(),
                d.getMaxInputPowerW(),
                d.getStorageGb(),
                d.getRamGb(),
                d.getBatteryMah(),
                d.getHasCellular()
        );
    }

    /* =========================
     * SMARTWATCH
     * ========================= */
    private EvaluationPayloadResDto.SmartwatchSpecs toSmartwatchSpecs(Long deviceId) {
        Smartwatch d = smartwatchRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("Smartwatch not found. deviceId=" + deviceId));

        return new EvaluationPayloadResDto.SmartwatchSpecs(
                d.getOs().name(),
                d.getCompatiblePhoneOs(),
                d.getChargingMethod() == null ? null : d.getChargingMethod().name(),
                d.getStrapSizeMm(),
                d.getCaseSizeMm(),
                d.getHasGps(),
                d.getHasCellular(),
                d.getBattery(),
                d.getWaterResistanceAtm()
        );
    }

    /* =========================
     * AUDIO
     * ========================= */
    private EvaluationPayloadResDto.AudioSpecs toAudioSpecs(Long deviceId) {
        Audio d = audioRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("Audio not found. deviceId=" + deviceId));

        return new EvaluationPayloadResDto.AudioSpecs(
                d.getFormFactor().name(),
                d.getSupportedCodecs(),
                d.getConnectionType().name(),
                d.getHasAnc(),
                d.getCaseChargingType() == null ? null : d.getCaseChargingType().name(),
                d.getDriverSizeMm(),
                d.getBatteryLifeHours(),
                d.getTotalBatteryLifeHours(),
                d.getWeightGram(),
                d.getHasMultipoint()
        );
    }

    /* =========================
     * KEYBOARD
     * ========================= */
    private EvaluationPayloadResDto.KeyboardSpecs toKeyboardSpecs(Long deviceId) {
        Keyboard d = keyboardRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("Keyboard not found. deviceId=" + deviceId));

        return new EvaluationPayloadResDto.KeyboardSpecs(
                d.getSupportedLayouts(),
                d.getConnectionType().name(),
                d.getKeyboardSize() == null ? null : d.getKeyboardSize().name(),
                d.getSwitchType() == null ? null : d.getSwitchType().name(),
                d.getMultiPairingCount(),
                d.getHasBacklight(),
                d.getHasRgb(),
                d.getHasHotswap(),
                d.getBatteryMah(),
                d.getWeightGram()
        );
    }

    /* =========================
     * MOUSE
     * ========================= */
    private EvaluationPayloadResDto.MouseSpecs toMouseSpecs(Long deviceId) {
        Mouse d = mouseRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("Mouse not found. deviceId=" + deviceId));

        return new EvaluationPayloadResDto.MouseSpecs(
                d.getConnectionType().name(),
                d.getGestureSupport(),
                d.getPowerSource() == null ? null : d.getPowerSource().name(),
                d.getMouseType() == null ? null : d.getMouseType().name(),
                d.getMaxDpi(),
                d.getButtonCount(),
                d.getMultiPairingCount(),
                d.getWeightGram(),
                d.getHasSilentClick()
        );
    }

    /* =========================
     * CHARGER
     * ========================= */
    private EvaluationPayloadResDto.ChargerSpecs toChargerSpecs(Long deviceId) {
        Charger d = chargerRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("Charger not found. deviceId=" + deviceId));

        return new EvaluationPayloadResDto.ChargerSpecs(
                d.getTotalPowerW(),
                d.getMaxSinglePortPowerW(),
                d.getPortConfiguration(),
                d.getSupportedProtocols(),
                d.getChargerType() == null ? null : d.getChargerType().name(),
                d.getIsGan(),
                d.getHasFoldablePlug(),
                d.getWeightGram()
        );
    }
}

