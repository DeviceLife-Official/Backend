-- V1__init.sql
-- MySQL 8.x / InnoDB / utf8mb4

CREATE TABLE `users` (
                         `userId` BIGINT NOT NULL AUTO_INCREMENT,
                         `username` VARCHAR(30) NULL,
                         `passwordHash` VARCHAR(255) NULL,
                         `nickname` VARCHAR(50) NOT NULL,
                         `email` VARCHAR(255) NULL,
                         `role` VARCHAR(20) NOT NULL DEFAULT 'USER',
                         `onboardingCompleted` TINYINT(1) NOT NULL DEFAULT 0,
                         `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `updatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (`userId`),
                         UNIQUE KEY `uk_users_username` (`username`),
                         UNIQUE KEY `uk_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `guestSessions` (
                                 `guestSessionId` CHAR(36) NOT NULL,
                                 `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `lastSeenAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `expiresAt` DATETIME NOT NULL,
                                 PRIMARY KEY (`guestSessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `guestData` (
                             `guestDataId` BIGINT NOT NULL AUTO_INCREMENT,
                             `guestSessionId` CHAR(36) NOT NULL,
                             `dataType` VARCHAR(30) NOT NULL,
                             `payload` JSON NOT NULL,
                             `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `updatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (`guestDataId`),
                             KEY `idx_guestData_guestSessionId` (`guestSessionId`),
                             CONSTRAINT `fk_guestData_guestSessions`
                                 FOREIGN KEY (`guestSessionId`) REFERENCES `guestSessions`(`guestSessionId`)
                                     ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `brands` (
                          `brandId` BIGINT NOT NULL AUTO_INCREMENT,
                          `brandName` VARCHAR(80) NOT NULL,
                          PRIMARY KEY (`brandId`),
                          UNIQUE KEY `uk_brands_brandName` (`brandName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `categories` (
                              `categoryId` BIGINT NOT NULL AUTO_INCREMENT,
                              `categoryKey` VARCHAR(30) NOT NULL,
                              `categoryName` VARCHAR(80) NOT NULL,
                              PRIMARY KEY (`categoryId`),
                              UNIQUE KEY `uk_categories_categoryKey` (`categoryKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tags` (
                        `tagId` BIGINT NOT NULL AUTO_INCREMENT,
                        `tagKey` VARCHAR(50) NOT NULL,
                        `tagLabel` VARCHAR(80) NOT NULL,
                        `tagType` VARCHAR(30) NOT NULL,
                        `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`tagId`),
                        UNIQUE KEY `uk_tags_tagKey` (`tagKey`),
                        KEY `idx_tags_tagType` (`tagType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `devices` (
                           `deviceId` BIGINT NOT NULL AUTO_INCREMENT,
                           `categoryId` BIGINT NOT NULL,
                           `brandId` BIGINT NOT NULL,
                           `modelName` VARCHAR(200) NOT NULL,
                           `modelCode` VARCHAR(120) NULL,
                           `releaseDate` DATE NULL,
                           `msrp` INT NULL,
                           `weightGram` INT NULL,
                           `batteryMah` INT NULL,
                           `ramGb` INT NULL,
                           `storageGb` INT NULL,
                           `screenInch` DECIMAL(4,2) NULL,
                           `specJson` JSON NULL,
                           `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `updatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`deviceId`),
                           KEY `idx_devices_categoryId` (`categoryId`),
                           KEY `idx_devices_brandId` (`brandId`),
                           KEY `idx_devices_modelName` (`modelName`),
                           CONSTRAINT `fk_devices_categories`
                               FOREIGN KEY (`categoryId`) REFERENCES `categories`(`categoryId`),
                           CONSTRAINT `fk_devices_brands`
                               FOREIGN KEY (`brandId`) REFERENCES `brands`(`brandId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `deviceImages` (
                                `deviceImageId` BIGINT NOT NULL AUTO_INCREMENT,
                                `deviceId` BIGINT NOT NULL,
                                `imageUrl` VARCHAR(500) NOT NULL,
                                `sortOrder` INT NOT NULL,
                                PRIMARY KEY (`deviceImageId`),
                                UNIQUE KEY `uk_deviceImages_device_sort` (`deviceId`, `sortOrder`),
                                CONSTRAINT `fk_deviceImages_devices`
                                    FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
                                        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `deviceOffers` (
                                `offerId` BIGINT NOT NULL AUTO_INCREMENT,
                                `deviceId` BIGINT NOT NULL,
                                `provider` VARCHAR(30) NOT NULL,
                                `externalItemId` VARCHAR(120) NOT NULL,
                                `title` VARCHAR(255) NULL,
                                `price` INT NULL,
                                `currency` VARCHAR(10) NOT NULL DEFAULT 'KRW',
                                `productUrl` VARCHAR(600) NULL,
                                `imageUrl` VARCHAR(500) NULL,
                                `collectedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`offerId`),
                                UNIQUE KEY `uk_deviceOffers_provider_item` (`provider`, `externalItemId`),
                                KEY `idx_deviceOffers_deviceId` (`deviceId`),
                                CONSTRAINT `fk_deviceOffers_devices`
                                    FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
                                        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `deviceTags` (
                              `deviceId` BIGINT NOT NULL,
                              `tagId` BIGINT NOT NULL,
                              PRIMARY KEY (`deviceId`, `tagId`),
                              KEY `idx_deviceTags_tagId` (`tagId`),
                              CONSTRAINT `fk_deviceTags_devices`
                                  FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
                                      ON DELETE CASCADE,
                              CONSTRAINT `fk_deviceTags_tags`
                                  FOREIGN KEY (`tagId`) REFERENCES `tags`(`tagId`)
                                      ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `userTags` (
                            `userId` BIGINT NOT NULL,
                            `tagId` BIGINT NOT NULL,
                            `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`userId`, `tagId`),
                            KEY `idx_userTags_tagId` (`tagId`),
                            CONSTRAINT `fk_userTags_users`
                                FOREIGN KEY (`userId`) REFERENCES `users`(`userId`)
                                    ON DELETE CASCADE,
                            CONSTRAINT `fk_userTags_tags`
                                FOREIGN KEY (`tagId`) REFERENCES `tags`(`tagId`)
                                    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `combos` (
                          `comboId` BIGINT NOT NULL AUTO_INCREMENT,
                          `userId` BIGINT NULL,
                          `guestSessionId` CHAR(36) NULL,
                          `comboName` VARCHAR(80) NOT NULL,
                          `pinnedAt` DATETIME NULL,
                          `totalPrice` INT NOT NULL DEFAULT 0,
                          `currentEvaluationId` BIGINT NULL,
                          `currentTotalScore` DECIMAL(6,2) NOT NULL DEFAULT 0.00,
                          `evaluatedAt` DATETIME NULL,
                          `evaluationVersion` VARCHAR(20) NOT NULL DEFAULT 'v1',
                          `deletedAt` DATETIME NULL,
                          `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `updatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`comboId`),
                          KEY `idx_combos_userId` (`userId`),
                          KEY `idx_combos_guestSessionId` (`guestSessionId`),
                          KEY `idx_combos_deletedAt` (`deletedAt`),
                          CONSTRAINT `fk_combos_users`
                              FOREIGN KEY (`userId`) REFERENCES `users`(`userId`)
                                  ON DELETE SET NULL,
                          CONSTRAINT `fk_combos_guestSessions`
                              FOREIGN KEY (`guestSessionId`) REFERENCES `guestSessions`(`guestSessionId`)
                                  ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `comboDevices` (
                                `comboId` BIGINT NOT NULL,
                                `deviceId` BIGINT NOT NULL,
                                `addedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`comboId`, `deviceId`),
                                KEY `idx_comboDevices_deviceId` (`deviceId`),
                                CONSTRAINT `fk_comboDevices_combos`
                                    FOREIGN KEY (`comboId`) REFERENCES `combos`(`comboId`)
                                        ON DELETE CASCADE,
                                CONSTRAINT `fk_comboDevices_devices`
                                    FOREIGN KEY (`deviceId`) REFERENCES `devices`(`deviceId`)
                                        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `evaluations` (
                               `evaluationId` BIGINT NOT NULL AUTO_INCREMENT,
                               `comboId` BIGINT NOT NULL,
                               `version` VARCHAR(20) NOT NULL,
                               `totalScore` DECIMAL(6,2) NOT NULL,
                               `scoreA` DECIMAL(6,2) NULL,
                               `scoreB` DECIMAL(6,2) NULL,
                               `scoreC` DECIMAL(6,2) NULL,
                               `scoreD` DECIMAL(6,2) NULL,
                               `reportJson` JSON NULL,
                               `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`evaluationId`),
                               KEY `idx_evaluations_comboId` (`comboId`),
                               CONSTRAINT `fk_evaluations_combos`
                                   FOREIGN KEY (`comboId`) REFERENCES `combos`(`comboId`)
                                       ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `evaluationExplanations` (
                                          `explanationId` BIGINT NOT NULL AUTO_INCREMENT,
                                          `evaluationId` BIGINT NOT NULL,
                                          `summary` TEXT NOT NULL,
                                          `pros` JSON NULL,
                                          `cons` JSON NULL,
                                          `bestFor` JSON NULL,
                                          `promptVersion` VARCHAR(20) NOT NULL DEFAULT 'p1',
                                          `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          PRIMARY KEY (`explanationId`),
                                          UNIQUE KEY `uk_evalExpl_evaluationId` (`evaluationId`),
                                          CONSTRAINT `fk_evalExpl_evaluations`
                                              FOREIGN KEY (`evaluationId`) REFERENCES `evaluations`(`evaluationId`)
                                                  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- combos.currentEvaluationId는 evaluations 테이블 만든 다음에 FK 걸어야 순환참조 안 꼬임
ALTER TABLE `combos`
    ADD CONSTRAINT `fk_combos_currentEvaluation`
        FOREIGN KEY (`currentEvaluationId`) REFERENCES `evaluations`(`evaluationId`)
            ON DELETE SET NULL;

CREATE TABLE `notices` (
                           `noticeId` BIGINT NOT NULL AUTO_INCREMENT,
                           `title` VARCHAR(200) NOT NULL,
                           `body` TEXT NOT NULL,
                           `isPublished` TINYINT(1) NOT NULL DEFAULT 0,
                           `publishedAt` DATETIME NULL,
                           `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `updatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`noticeId`),
                           KEY `idx_notices_isPublished` (`isPublished`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `faqs` (
                        `faqId` BIGINT NOT NULL AUTO_INCREMENT,
                        `question` VARCHAR(255) NOT NULL,
                        `answer` TEXT NOT NULL,
                        `sortOrder` INT NOT NULL DEFAULT 0,
                        `isPublished` TINYINT(1) NOT NULL DEFAULT 0,
                        PRIMARY KEY (`faqId`),
                        KEY `idx_faqs_isPublished_sort` (`isPublished`, `sortOrder`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `policies` (
                            `policyId` BIGINT NOT NULL AUTO_INCREMENT,
                            `policyType` VARCHAR(30) NOT NULL,
                            `version` VARCHAR(30) NOT NULL,
                            `body` TEXT NOT NULL,
                            `publishedAt` DATETIME NULL,
                            `isActive` TINYINT(1) NOT NULL DEFAULT 0,
                            PRIMARY KEY (`policyId`),
                            UNIQUE KEY `uk_policies_type_version` (`policyType`, `version`),
                            KEY `idx_policies_type_active` (`policyType`, `isActive`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `inquiries` (
                             `inquiryId` BIGINT NOT NULL AUTO_INCREMENT,
                             `userId` BIGINT NULL,
                             `email` VARCHAR(255) NULL,
                             `subject` VARCHAR(200) NOT NULL,
                             `message` TEXT NOT NULL,
                             `status` VARCHAR(20) NOT NULL DEFAULT 'OPEN',
                             `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `updatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (`inquiryId`),
                             KEY `idx_inquiries_userId` (`userId`),
                             KEY `idx_inquiries_status` (`status`),
                             CONSTRAINT `fk_inquiries_users`
                                 FOREIGN KEY (`userId`) REFERENCES `users`(`userId`)
                                     ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
