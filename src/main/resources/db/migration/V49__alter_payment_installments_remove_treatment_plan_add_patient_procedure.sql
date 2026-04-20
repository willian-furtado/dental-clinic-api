-- Remove treatment_plan_id and add patient_procedure_id to payment_installments table

-- Drop the existing index on treatment_plan_id
DROP INDEX IF EXISTS idx_payment_installments_treatment_plan_id;

-- Drop the foreign key constraint and column
ALTER TABLE payment_installments DROP CONSTRAINT IF EXISTS payment_installments_treatment_plan_id_fkey;
ALTER TABLE payment_installments DROP COLUMN IF EXISTS treatment_plan_id;

-- Add patient_procedure_id column
ALTER TABLE payment_installments ADD COLUMN patient_procedure_id UUID NOT NULL;

-- Add foreign key constraint to patient_procedures
ALTER TABLE payment_installments ADD CONSTRAINT payment_installments_patient_procedure_id_fkey 
    FOREIGN KEY (patient_procedure_id) REFERENCES patient_procedures(id) ON DELETE CASCADE;

-- Create index for patient_procedure_id
CREATE INDEX idx_payment_installments_patient_procedure_id ON payment_installments(patient_procedure_id);