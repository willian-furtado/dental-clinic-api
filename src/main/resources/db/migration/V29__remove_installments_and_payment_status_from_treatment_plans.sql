-- Remove installments and payment_status columns from treatment_plans table
ALTER TABLE treatment_plans DROP COLUMN IF EXISTS installments;
ALTER TABLE treatment_plans DROP COLUMN IF EXISTS payment_status;
ALTER TABLE treatment_plans DROP COLUMN IF EXISTS payment_method;