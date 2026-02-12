ALTER TABLE appointments
ADD COLUMN patient_procedure_id uuid NULL;

ALTER TABLE appointments
ADD CONSTRAINT fk_appointment_patient_procedure
FOREIGN KEY (patient_procedure_id)
REFERENCES patient_procedures(id);
