-- V8__seed_onboarding_tags.sql
-- 온보딩(라이프스타일) 태그 시드 + 기존 키 정리(가능한 범위)

-- 1) 기존에 'Tour/portability' 같은 키로 들어간 데이터가 있으면 'Tour'로 정리
--    (Tour 키가 이미 있으면 중복될 수 있으니, 그 경우에는 수동 정리가 필요할 수 있어요.)
UPDATE tags
SET tagKey = 'Tour',
    tagLabel = '# Tour/portability',
    tagType = 'LIFESTYLE'
WHERE tagKey = 'Tour/portability';

-- 2) 기존 목적 태그 라벨을 UI에 맞게 정리
UPDATE tags SET tagLabel = '# Office/portability', tagType='LIFESTYLE' WHERE tagKey='Office';
UPDATE tags SET tagLabel = '# Developer',          tagType='LIFESTYLE' WHERE tagKey='Developer';
UPDATE tags SET tagLabel = '# Game',               tagType='LIFESTYLE' WHERE tagKey='Game';
UPDATE tags SET tagLabel = '# Study',              tagType='LIFESTYLE' WHERE tagKey='Study';
UPDATE tags SET tagLabel = '# Video-editing',      tagType='LIFESTYLE' WHERE tagKey='Video-editing';
UPDATE tags SET tagLabel = '# Tour/portability',   tagType='LIFESTYLE' WHERE tagKey='Tour';

-- 3) 없으면 삽입 / 있으면 label만 최신으로 업데이트 (idempotent)
INSERT INTO tags (tagKey, tagLabel, tagType)
VALUES
-- 목적(주된 용도)
('Office',        '# Office/portability', 'LIFESTYLE'),
('Tour',          '# Tour/portability',   'LIFESTYLE'),
('Developer',     '# Developer',          'LIFESTYLE'),
('Game',          '# Game',               'LIFESTYLE'),
('Study',         '# Study',              'LIFESTYLE'),
('Video-editing', '# Video-editing',      'LIFESTYLE'),

-- 중요 기준(왼쪽 그룹)  ※ key는 코드용으로만 쓸 거라 영어로 뒀어요. label이 화면 표시.
('Performance',   '고성능',      'LIFESTYLE'),
('Value',         '가성비',      'LIFESTYLE'),
('Portability',   '휴대성',      'LIFESTYLE'),
('BatteryLife',   '배터리 수명', 'LIFESTYLE'),
('DesignColor',   '디자인/컬러', 'LIFESTYLE'),

-- 선호 브랜드(오른쪽 그룹)
('Apple',         'Apple',     'LIFESTYLE'),
('Samsung',       'Samsung',   'LIFESTYLE'),
('Sony',          'Sony',      'LIFESTYLE'),
('Logitech',      'Logitech',  'LIFESTYLE'),
('Any',           '상관없음',   'LIFESTYLE')
    ON DUPLICATE KEY UPDATE
                         tagLabel = VALUES(tagLabel),
                         tagType  = VALUES(tagType);
