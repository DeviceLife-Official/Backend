-- V12__evaluation_version_bigint.sql
-- 1) combos.evaluationVersion: VARCHAR -> BIGINT
-- 2) evaluations.version: VARCHAR -> BIGINT
-- 3) (옵션) evaluations.comboId UNIQUE (combo당 1개 평가만 유지 정책)
-- 4) (옵션) scoreD 컬럼 제거

-- ============================================================
-- 0. 사전 점검(선택)
-- ============================================================
-- SELECT evaluationVersion, COUNT(*) FROM combos GROUP BY evaluationVersion;
-- SELECT version, COUNT(*) FROM evaluations GROUP BY version;

-- ============================================================
-- 1. combos.evaluationVersion 타입 변경
-- ============================================================

-- 1-1) 기존 컬럼이 VARCHAR라면, 우선 새로운 BIGINT 컬럼을 만들어 안전 마이그레이션
ALTER TABLE `combos`
    ADD COLUMN `evaluationVersion_tmp` BIGINT NOT NULL DEFAULT 0 AFTER `evaluatedAt`;

-- 1-2) 기존 값 마이그레이션
-- 과거에 "v1" 같은 문자열이 들어있던 케이스 처리
--  - v1 -> 1
--  - 숫자 문자열이면 그대로
--  - NULL/기타 -> 0
UPDATE `combos`
SET `evaluationVersion_tmp` =
        CASE
            WHEN `evaluationVersion` IS NULL THEN 0
            WHEN `evaluationVersion` REGEXP '^v[0-9]+$' THEN CAST(SUBSTRING(`evaluationVersion`, 2) AS UNSIGNED)
            WHEN `evaluationVersion` REGEXP '^[0-9]+$' THEN CAST(`evaluationVersion` AS UNSIGNED)
            ELSE 0
            END;

-- 1-3) 기존 컬럼 제거 후 rename
ALTER TABLE `combos`
DROP COLUMN `evaluationVersion`,
    CHANGE COLUMN `evaluationVersion_tmp` `evaluationVersion` BIGINT NOT NULL DEFAULT 0;

-- ============================================================
-- 2. evaluations.version 타입 변경
-- ============================================================

ALTER TABLE `evaluations`
    ADD COLUMN `version_tmp` BIGINT NOT NULL DEFAULT 0 AFTER `comboId`;

UPDATE `evaluations`
SET `version_tmp` =
        CASE
            WHEN `version` IS NULL THEN 0
            WHEN `version` REGEXP '^v[0-9]+$' THEN CAST(SUBSTRING(`version`, 2) AS UNSIGNED)
            WHEN `version` REGEXP '^[0-9]+$' THEN CAST(`version` AS UNSIGNED)
            ELSE 0
            END;

ALTER TABLE `evaluations`
DROP COLUMN `version`,
    CHANGE COLUMN `version_tmp` `version` BIGINT NOT NULL;

-- ============================================================
-- 3. (강추) combo당 evaluations 1개만 유지 정책을 DB로 강제
--    - 이미 comboId당 여러 row가 있으면 UNIQUE 추가가 실패함
--    - 지금부터는 서비스에서 갱신 시 old delete 하니까 문제 없어야 함
-- ============================================================

-- 만약 기존 데이터가 이미 여러개면 먼저 정리해야 함:
-- 예: 최신 evaluationId만 남기고 삭제하는 쿼리 필요(원하면 만들어줄게)

ALTER TABLE `evaluations`
    ADD UNIQUE KEY `uk_evaluations_comboId` (`comboId`);

-- ============================================================
-- 4. (옵션) scoreD 컬럼 제거
-- ============================================================
-- 엔티티에서 scoreD 주석처리했으면 테이블도 정리하는 게 맞음
-- 컬럼이 실제로 존재할 때만 실행해야 함 (없으면 에러)
-- 필요 없으면 아래 블록 주석 유지

ALTER TABLE `evaluations`
DROP COLUMN `scoreD`;