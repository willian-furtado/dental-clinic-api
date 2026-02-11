ALTER TABLE treatment_plans ADD COLUMN IF NOT EXISTS code BIGINT UNIQUE;

CREATE INDEX IF NOT EXISTS idx_treatment_plans_code ON treatment_plans(code);
