-- Add weight column to tablets table
-- Weight is stored in grams (g) as INTEGER for precise calculations

ALTER TABLE tablets
    ADD COLUMN weightGram INT NULL COMMENT '무게 (그램)';
