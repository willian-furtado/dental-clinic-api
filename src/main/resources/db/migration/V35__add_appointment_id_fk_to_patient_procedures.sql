ALTER TABLE patient_procedures 
ADD COLUMN appointment_id UUID NULL;

ALTER TABLE patient_procedures 
ADD CONSTRAINT fk_patient_procedures_appointment 
FOREIGN KEY (appointment_id) 
REFERENCES appointments(id);
