ALTER TABLE treatment_plan_procedures ADD COLUMN procedure_clinic_id UUID REFERENCES procedures_clinic(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_treatment_plan_procedures_procedure_clinic_id ON treatment_plan_procedures(procedure_clinic_id);
