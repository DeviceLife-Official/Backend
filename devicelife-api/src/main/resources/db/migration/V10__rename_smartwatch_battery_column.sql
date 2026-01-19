-- V10__rename_smartwatch_battery_column.sql
-- smartwatches 테이블의 batteryLifeDays 컬럼명을 battery로 변경

ALTER TABLE `smartwatches`
    CHANGE COLUMN `batteryLifeDays` `battery` INT NULL COMMENT '배터리 수명 (일)';
