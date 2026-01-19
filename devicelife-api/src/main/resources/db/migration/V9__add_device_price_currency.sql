-- V9__add_device_price_currency.sql
-- devices 테이블에 priceCurrency 컬럼 추가

ALTER TABLE `devices`
    ADD COLUMN `priceCurrency` VARCHAR(10) NULL AFTER `price`;

-- 기존 데이터는 KRW로 설정 (기본값)
UPDATE `devices` SET `priceCurrency` = 'KRW' WHERE `priceCurrency` IS NULL;
