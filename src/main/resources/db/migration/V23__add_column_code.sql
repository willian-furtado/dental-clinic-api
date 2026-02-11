CREATE SEQUENCE IF NOT EXISTS treatment_plans_code_seq;

-- Criar índice
CREATE INDEX IF NOT EXISTS idx_treatment_plans_code ON treatment_plans(code);