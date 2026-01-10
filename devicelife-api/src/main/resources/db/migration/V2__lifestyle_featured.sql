-- 태그별 대표 추천 세트(=조합 1개)
CREATE TABLE `lifestyleFeaturedSets` (
                                         `featuredSetId` BIGINT NOT NULL AUTO_INCREMENT,
                                         `tagId` BIGINT NOT NULL,
                                         `isActive` TINYINT(1) NOT NULL DEFAULT 1,
                                         `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         `updatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`featuredSetId`),
                                         UNIQUE KEY `uk_lfs_tagId` (`tagId`),
                                         CONSTRAINT `fk_lfs_tags`
                                             FOREIGN KEY (`tagId`) REFERENCES `tags` (`tagId`)
                                                 ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 세트에 들어가는 3개 기기
CREATE TABLE `lifestyleFeaturedSetDevices` (
                                               `featuredSetId` BIGINT NOT NULL,
                                               `slot` TINYINT NOT NULL,         -- 1~3
                                               `deviceId` BIGINT NOT NULL,
                                               `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               PRIMARY KEY (`featuredSetId`, `slot`),
                                               UNIQUE KEY `uk_lfsd_device` (`featuredSetId`, `deviceId`),
                                               CONSTRAINT `fk_lfsd_set`
                                                   FOREIGN KEY (`featuredSetId`) REFERENCES `lifestyleFeaturedSets` (`featuredSetId`)
                                                       ON DELETE CASCADE,
                                               CONSTRAINT `fk_lfsd_device`
                                                   FOREIGN KEY (`deviceId`) REFERENCES `devices` (`deviceId`)
                                                       ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
