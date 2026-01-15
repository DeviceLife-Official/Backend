-- V5__add_user_provider.sql

ALTER TABLE `users`
    ADD COLUMN `provider` VARCHAR(20) NOT NULL DEFAULT 'GENERAL',
    ADD COLUMN `providerId` VARCHAR(255) NULL;

-- provider가 NULL로 들어갈 가능성 대비
UPDATE `users`
SET `provider` = 'GENERAL'
WHERE `provider` IS NULL;

-- 동일 provider 내 providerId 중복 방지
ALTER TABLE `users`
    ADD UNIQUE KEY `uk_users_provider_providerId` (`provider`, `providerId`);

-- 소셜 로그인에서는 전화번호 null
ALTER TABLE `users`
    MODIFY COLUMN `phoneNumber` VARCHAR(20) NULL DEFAULT NULL;