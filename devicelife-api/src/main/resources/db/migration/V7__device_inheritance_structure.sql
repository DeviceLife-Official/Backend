-- V7__device_inheritance_structure.sql
-- 디바이스 상속 구조로 변경 (JOINED 전략)
-- 기존 devices 테이블을 부모 테이블로 변경하고, 각 디바이스 타입별 자식 테이블 생성

-- ============================================================
-- 1. devices 테이블 구조 변경 (부모 테이블)
-- ============================================================

-- 1-1. 새 컬럼 추가
ALTER TABLE `devices`
    ADD COLUMN `deviceType` VARCHAR(30) NOT NULL DEFAULT 'SMARTPHONE' AFTER `deviceId`,
    ADD COLUMN `name` VARCHAR(200) NULL AFTER `brandId`,
    ADD COLUMN `price` INT NULL AFTER `modelCode`,
    ADD COLUMN `colorHex` VARCHAR(7) NULL AFTER `releaseDate`,
    ADD COLUMN `imageUrl` VARCHAR(500) NULL AFTER `colorHex`;

-- 1-2. 기존 데이터 마이그레이션
-- modelName -> name, msrp -> price
UPDATE `devices` SET `name` = `modelName` WHERE `name` IS NULL;
UPDATE `devices` SET `price` = `msrp` WHERE `price` IS NULL;

-- 1-3. name 컬럼 NOT NULL 설정
ALTER TABLE `devices` MODIFY COLUMN `name` VARCHAR(200) NOT NULL;

-- 1-4. deviceType 인덱스 추가
ALTER TABLE `devices` ADD INDEX `idx_devices_deviceType` (`deviceType`);

-- 1-5. 기존 categoryId FK 제거 (deviceType으로 대체)
ALTER TABLE `devices` DROP FOREIGN KEY `fk_devices_categories`;
ALTER TABLE `devices` DROP INDEX `idx_devices_categoryId`;

-- ============================================================
-- 2. 메인 디바이스 테이블 생성
-- ============================================================

-- 2-1. 스마트폰 (Smartphone)
CREATE TABLE `smartphones` (
    `deviceId` BIGINT NOT NULL,
    `os` VARCHAR(20) NOT NULL COMMENT 'iOS, ANDROID',
    `chargingPort` VARCHAR(20) NOT NULL COMMENT 'USB_C, LIGHTNING, MICRO_USB',
    `wirelessCharging` VARCHAR(20) NULL COMMENT 'MAGSAFE, QI, NONE',
    `maxInputPowerW` INT NULL COMMENT '최대 입력 전력 (W)',
    `biometricType` VARCHAR(30) NULL COMMENT 'FACE_ID, FINGERPRINT, ON_SCREEN_FINGERPRINT',
    `storageGb` INT NULL,
    `ramGb` INT NULL,
    `screenInch` DOUBLE NULL,
    `batteryMah` INT NULL,
    PRIMARY KEY (`deviceId`),
    CONSTRAINT `fk_smartphones_devices`
        FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='스마트폰 - 생태계 호환성의 절대적인 기준점';

-- 2-2. 노트북 (Laptop)
CREATE TABLE `laptops` (
    `deviceId` BIGINT NOT NULL,
    `os` VARCHAR(20) NOT NULL COMMENT 'MACOS, WINDOWS, CHROMEOS, LINUX',
    `cpu` VARCHAR(100) NULL COMMENT 'M3, Intel Core i7, Ryzen 7 등',
    `gpu` VARCHAR(100) NULL COMMENT '내장 그래픽, RTX 4050 등',
    `minRequiredPowerW` INT NOT NULL COMMENT '충전에 필요한 최소 전력 (W)',
    `chargingMethod` VARCHAR(20) NOT NULL COMMENT 'USB_C, DC_ADAPTER',
    `hasHdmi` TINYINT(1) NULL DEFAULT 0,
    `hasThunderbolt` TINYINT(1) NULL DEFAULT 0,
    `hasUsbA` TINYINT(1) NULL DEFAULT 0,
    `usbCPortCount` INT NULL,
    `weightKg` DECIMAL(4,2) NULL,
    `ramGb` INT NULL,
    `storageGb` INT NULL,
    `screenInch` DOUBLE NULL,
    `batteryWh` INT NULL,
    PRIMARY KEY (`deviceId`),
    CONSTRAINT `fk_laptops_devices`
        FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='노트북 - 가장 많은 전력을 필요로 하는 업무 생산성의 핵심';

-- 2-3. 태블릿 (Tablet)
CREATE TABLE `tablets` (
    `deviceId` BIGINT NOT NULL,
    `os` VARCHAR(20) NOT NULL COMMENT 'IPADOS, ANDROID, WINDOWS',
    `screenInch` DOUBLE NOT NULL,
    `stylusType` VARCHAR(30) NULL COMMENT 'APPLE_PENCIL_2, S_PEN, NONE 등',
    `keyboardConnection` VARCHAR(30) NULL COMMENT 'SMART_CONNECTOR, BLUETOOTH 등',
    `chargingPort` VARCHAR(20) NOT NULL COMMENT 'USB_C, LIGHTNING',
    `maxInputPowerW` INT NULL,
    `storageGb` INT NULL,
    `ramGb` INT NULL,
    `batteryMah` INT NULL,
    `hasCellular` TINYINT(1) NULL DEFAULT 0,
    PRIMARY KEY (`deviceId`),
    CONSTRAINT `fk_tablets_devices`
        FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='태블릿 - 모바일과 PC 사이를 잇는 하이브리드 기기';

-- ============================================================
-- 3. 서브 디바이스 테이블 생성
-- ============================================================

-- 3-1. 스마트워치 (Smartwatch)
CREATE TABLE `smartwatches` (
    `deviceId` BIGINT NOT NULL,
    `os` VARCHAR(20) NOT NULL COMMENT 'WATCHOS, WEAROS, TIZEN, GARMIN_OS',
    `compatiblePhoneOs` JSON NULL COMMENT '호환 스마트폰 OS 목록 (["iOS"], ["Android"] 등)',
    `chargingMethod` VARCHAR(30) NULL COMMENT 'PROPRIETARY_PUCK, QI_WIRELESS 등',
    `strapSizeMm` INT NULL COMMENT '스트랩 사이즈 (mm)',
    `caseSizeMm` INT NULL COMMENT '케이스 사이즈 (mm)',
    `hasGps` TINYINT(1) NULL DEFAULT 0,
    `hasCellular` TINYINT(1) NULL DEFAULT 0,
    `batteryLifeDays` INT NULL,
    `waterResistanceAtm` INT NULL COMMENT '방수 등급 (ATM)',
    PRIMARY KEY (`deviceId`),
    CONSTRAINT `fk_smartwatches_devices`
        FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='스마트워치 - 스마트폰 OS에 따라 호환성이 결정되는 폐쇄적 기기';

-- 3-2. 오디오 (Audio - 이어폰/헤드폰)
CREATE TABLE `audioDevices` (
    `deviceId` BIGINT NOT NULL,
    `formFactor` VARCHAR(20) NOT NULL COMMENT 'IN_EAR, OPEN_EAR, ON_EAR, OVER_EAR',
    `supportedCodecs` JSON NULL COMMENT '지원 코덱 목록 (["SBC","AAC","LDAC"] 등)',
    `connectionType` VARCHAR(20) NOT NULL COMMENT 'TWS, BLUETOOTH, WIRED_3_5MM, WIRED_USB_C',
    `hasAnc` TINYINT(1) NULL DEFAULT 0 COMMENT '노이즈 캔슬링 지원',
    `caseChargingType` VARCHAR(30) NULL COMMENT 'USB_C, LIGHTNING, WIRELESS 등',
    `driverSizeMm` DOUBLE NULL,
    `batteryLifeHours` INT NULL COMMENT '본체 배터리 수명 (시간)',
    `totalBatteryLifeHours` INT NULL COMMENT '케이스 포함 총 배터리 (시간)',
    `weightGram` INT NULL,
    `hasMultipoint` TINYINT(1) NULL DEFAULT 0 COMMENT '멀티포인트 지원',
    PRIMARY KEY (`deviceId`),
    CONSTRAINT `fk_audioDevices_devices`
        FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='오디오 - 코덱에 따른 음질 최적화가 중요한 기기';

-- 3-3. 키보드 (Keyboard)
CREATE TABLE `keyboards` (
    `deviceId` BIGINT NOT NULL,
    `supportedLayouts` JSON NULL COMMENT '지원 레이아웃 (["Windows","macOS","iOS"] 등)',
    `connectionType` VARCHAR(30) NOT NULL COMMENT 'BLUETOOTH, DONGLE_2_4GHZ, WIRED_USB 등',
    `keyboardSize` VARCHAR(20) NULL COMMENT 'FULL, TKL, COMPACT_75, MINI_60',
    `switchType` VARCHAR(20) NULL COMMENT 'BLUE, RED, BROWN, SCISSOR, TOPRE 등',
    `multiPairingCount` INT NULL COMMENT '멀티 페어링 가능 기기 수',
    `hasBacklight` TINYINT(1) NULL DEFAULT 0,
    `hasRgb` TINYINT(1) NULL DEFAULT 0,
    `hasHotswap` TINYINT(1) NULL DEFAULT 0 COMMENT '핫스왑 지원',
    `batteryMah` INT NULL,
    `weightGram` INT NULL,
    PRIMARY KEY (`deviceId`),
    CONSTRAINT `fk_keyboards_devices`
        FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='키보드 - OS별 키 배열 호환성이 중요한 기기';

-- 3-4. 마우스 (Mouse)
CREATE TABLE `mice` (
    `deviceId` BIGINT NOT NULL,
    `connectionType` VARCHAR(30) NOT NULL COMMENT 'BLUETOOTH, DONGLE_2_4GHZ, WIRED_USB 등',
    `gestureSupport` JSON NULL COMMENT '제스처 지원 OS (["macOS","Windows","iPadOS"] 등)',
    `powerSource` VARCHAR(30) NULL COMMENT 'USB_C_RECHARGEABLE, AA_BATTERY 등',
    `mouseType` VARCHAR(20) NULL COMMENT 'STANDARD, VERTICAL, TRACKBALL, GAMING',
    `maxDpi` INT NULL,
    `buttonCount` INT NULL,
    `multiPairingCount` INT NULL,
    `weightGram` INT NULL,
    `hasSilentClick` TINYINT(1) NULL DEFAULT 0,
    PRIMARY KEY (`deviceId`),
    CONSTRAINT `fk_mice_devices`
        FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='마우스 - 연결 방식과 OS별 제스처 지원이 핵심';

-- ============================================================
-- 4. 전력 공급 테이블 생성
-- ============================================================

-- 4-1. 충전기 (Charger)
CREATE TABLE `chargers` (
    `deviceId` BIGINT NOT NULL,
    `totalPowerW` INT NOT NULL COMMENT '총 출력 (W)',
    `maxSinglePortPowerW` INT NOT NULL COMMENT '단일 포트 최대 출력 (W)',
    `portConfiguration` JSON NULL COMMENT '포트 구성 (["C","C","A"] 등)',
    `supportedProtocols` JSON NULL COMMENT '지원 프로토콜 (["PD_3_0","PPS","QC"] 등)',
    `chargerType` VARCHAR(30) NULL COMMENT 'ADAPTER, DESKTOP_STATION, WIRELESS_STAND 등',
    `isGan` TINYINT(1) NULL DEFAULT 0 COMMENT 'GaN 충전기 여부',
    `hasFoldablePlug` TINYINT(1) NULL DEFAULT 0 COMMENT '접이식 플러그',
    `weightGram` INT NULL,
    PRIMARY KEY (`deviceId`),
    CONSTRAINT `fk_chargers_devices`
        FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='충전기 - 모든 기기에 전력을 공급하는 기기';

-- ============================================================
-- 5. 인덱스 정리 (컬럼 삭제 전에 먼저 실행)
-- ============================================================

-- modelName 컬럼 삭제 전에 인덱스 먼저 삭제 (MySQL은 컬럼 삭제 시 인덱스도 자동 삭제하지만, 명시적으로 먼저 삭제)
ALTER TABLE `devices` DROP INDEX `idx_devices_modelName`;

-- ============================================================
-- 6. 기존 불필요 컬럼 삭제 (자식 테이블로 이동됨)
-- ============================================================

ALTER TABLE `devices`
    DROP COLUMN `categoryId`,
    DROP COLUMN `modelName`,
    DROP COLUMN `msrp`,
    DROP COLUMN `weightGram`,
    DROP COLUMN `batteryMah`,
    DROP COLUMN `ramGb`,
    DROP COLUMN `storageGb`,
    DROP COLUMN `screenInch`,
    DROP COLUMN `specJson`;

-- ============================================================
-- 7. 새 인덱스 생성
-- ============================================================

ALTER TABLE `devices` ADD INDEX `idx_devices_name` (`name`);
