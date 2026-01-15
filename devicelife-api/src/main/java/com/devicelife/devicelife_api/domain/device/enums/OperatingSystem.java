package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 운영체제 종류
 */
@Getter
@RequiredArgsConstructor
public enum OperatingSystem {

    // 모바일 OS
    IOS("iOS", OsFamily.APPLE),
    ANDROID("Android", OsFamily.GOOGLE),
    IPADOS("iPadOS", OsFamily.APPLE),

    // 데스크탑 OS
    MACOS("macOS", OsFamily.APPLE),
    WINDOWS("Windows", OsFamily.MICROSOFT),
    CHROMEOS("ChromeOS", OsFamily.GOOGLE),
    LINUX("Linux", OsFamily.OPEN_SOURCE),

    // 웨어러블 OS
    WATCHOS("watchOS", OsFamily.APPLE),
    WEAROS("WearOS", OsFamily.GOOGLE),
    TIZEN("Tizen", OsFamily.SAMSUNG),
    GARMIN_OS("Garmin OS", OsFamily.GARMIN);

    private final String displayName;
    private final OsFamily family;

    public enum OsFamily {
        APPLE("Apple"),
        GOOGLE("Google"),
        MICROSOFT("Microsoft"),
        SAMSUNG("Samsung"),
        GARMIN("Garmin"),
        OPEN_SOURCE("Open Source");

        private final String displayName;

        OsFamily(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Apple 생태계 호환성 체크
     */
    public boolean isAppleEcosystem() {
        return family == OsFamily.APPLE;
    }

    /**
     * Android/Google 생태계 호환성 체크
     */
    public boolean isAndroidEcosystem() {
        return family == OsFamily.GOOGLE || family == OsFamily.SAMSUNG;
    }
}
