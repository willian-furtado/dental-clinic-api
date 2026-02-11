-- Add payment_status column to treatment_plans table
ALTER TABLE treatment_plans ADD COLUMN payment_status VARCHAR(20) NOT NULL DEFAULT 'pending';

UPDATE treatment_plans SET payment_status = 'pending' WHERE payment_status IS NULL;