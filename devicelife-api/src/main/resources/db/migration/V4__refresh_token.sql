CREATE TABLE `refreshToken` (
                                `refreshTokenId` BIGINT NOT NULL AUTO_INCREMENT,
                                `tokenHash` VARCHAR(255) NOT NULL,
                                `userId` BIGINT NOT NULL,
                                `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`refreshTokenId`),
                                UNIQUE KEY `uk_refreshToken_tokenHash` (`tokenHash`),
                                KEY `idx_refreshToken_userId` (`userId`),
                                CONSTRAINT `fk_refreshToken_users`
                                    FOREIGN KEY (`userId`) REFERENCES `users`(`userId`)
                                        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;