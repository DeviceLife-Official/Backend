package com.devicelife.devicelife_api.domain.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationPayloadResDto {

    private Long combinationId;
    private Long evaluationVersion;
    private String jobId;
    private List<DeviceDto> devices;

    /* =========================
     * Device wrapper
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceDto {
        private Long deviceId;
        private DeviceType type;
        private Specs specs;
    }

    public enum DeviceType {
        SMARTPHONE,
        LAPTOP,
        TABLET,
        SMARTWATCH,
        AUDIO,
        KEYBOARD,
        MOUSE,
        CHARGER
    }

    /* =========================
     * Polymorphic specs
     * - specs.type 값을 따로 만들지 않고,
     * - "device.type" 값을 기준으로 specs 타입을 결정하고 싶다면
     *   (서버에서 직접 매핑해 내려주는 방식)
     * ========================= */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SmartphoneSpecs.class, name = "SMARTPHONE"),
            @JsonSubTypes.Type(value = LaptopSpecs.class, name = "LAPTOP"),
            @JsonSubTypes.Type(value = TabletSpecs.class, name = "TABLET"),
            @JsonSubTypes.Type(value = SmartwatchSpecs.class, name = "SMARTWATCH"),
            @JsonSubTypes.Type(value = AudioSpecs.class, name = "AUDIO"),
            @JsonSubTypes.Type(value = KeyboardSpecs.class, name = "KEYBOARD"),
            @JsonSubTypes.Type(value = MouseSpecs.class, name = "MOUSE"),
            @JsonSubTypes.Type(value = ChargerSpecs.class, name = "CHARGER")
    })
    public interface Specs {}

    /* =========================
     * SMARTPHONE specs
     * (smartphones 테이블 기준)
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SmartphoneSpecs implements Specs {
        private String os;                 // iOS, ANDROID
        private String chargingPort;       // USB_C, LIGHTNING...
        private String wirelessCharging;   // MAGSAFE, QI, NONE
        private Integer maxInputPowerW;
        private String biometricType;
        private Integer storageGb;
        private Integer ramGb;
        private Double screenInch;
        private Integer batteryMah;
    }

    /* =========================
     * LAPTOP specs
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LaptopSpecs implements Specs {
        private String os;
        private String cpu;
        private String gpu;
        private Integer minRequiredPowerW;
        private String chargingMethod;     // USB_C, DC_ADAPTER
        private Boolean hasHdmi;
        private Boolean hasThunderbolt;
        private Boolean hasUsbA;
        private Integer usbCPortCount;
        private BigDecimal weightKg;
        private Integer ramGb;
        private Integer storageGb;
        private Double screenInch;
        private Integer batteryWh;
    }

    /* =========================
     * TABLET specs
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TabletSpecs implements Specs {
        private String os;
        private Double screenInch;
        private String stylusType;
        private String keyboardConnection;
        private String chargingPort;
        private Integer maxInputPowerW;
        private Integer storageGb;
        private Integer ramGb;
        private Integer batteryMah;
        private Boolean hasCellular;
    }

    /* =========================
     * SMARTWATCH specs
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SmartwatchSpecs implements Specs {
        private String os;
        private Object compatiblePhoneOs;   // JSON 컬럼이면 List<String>으로 바꿔도 됨
        private String chargingMethod;
        private Integer strapSizeMm;
        private Integer caseSizeMm;
        private Boolean hasGps;
        private Boolean hasCellular;
        private Integer battery;            // V10 반영
        private Integer waterResistanceAtm;
    }

    /* =========================
     * AUDIO specs
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AudioSpecs implements Specs {
        private String formFactor;
        private Object supportedCodecs;     // List<String> 권장
        private String connectionType;
        private Boolean hasAnc;
        private String caseChargingType;
        private Double driverSizeMm;
        private Integer batteryLifeHours;
        private Integer totalBatteryLifeHours;
        private Integer weightGram;
        private Boolean hasMultipoint;
    }

    /* =========================
     * KEYBOARD specs
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyboardSpecs implements Specs {
        private Object supportedLayouts;    // List<String> 권장
        private String connectionType;
        private String keyboardSize;
        private String switchType;
        private Integer multiPairingCount;
        private Boolean hasBacklight;
        private Boolean hasRgb;
        private Boolean hasHotswap;
        private Integer batteryMah;
        private Integer weightGram;
    }

    /* =========================
     * MOUSE specs
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MouseSpecs implements Specs {
        private String connectionType;
        private Object gestureSupport;      // List<String> 권장
        private String powerSource;
        private String mouseType;
        private Integer maxDpi;
        private Integer buttonCount;
        private Integer multiPairingCount;
        private Integer weightGram;
        private Boolean hasSilentClick;
    }

    /* =========================
     * CHARGER specs
     * ========================= */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChargerSpecs implements Specs {
        private Integer totalPowerW;
        private Integer maxSinglePortPowerW;
        private Object portConfiguration;   // List<String> 권장
        private Object supportedProtocols;  // List<String> 권장
        private String chargerType;
        private Boolean isGan;
        private Boolean hasFoldablePlug;
        private Integer weightGram;
    }
}
