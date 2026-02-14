ALTER TABLE appointments 
DROP CONSTRAINT IF EXISTS fk_appointment_patient_procedure;

ALTER TABLE appointments 
ADD CONSTRAINT fk_appointment_patient_procedure 
FOREIGN KEY (patient_procedure_id) 
REFERENCES patient_procedures(id) 
ON DELETE SET NULL;

COMMENT ON CONSTRAINT fk_appointment_patient_procedure ON appointments IS 'Foreign key constraint with CASCADE DELETE SET NULL to handle appointment deletion properly';