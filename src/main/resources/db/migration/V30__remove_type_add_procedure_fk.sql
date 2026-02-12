
ALTER TABLE public.appointments
ADD COLUMN fk_procedure_clinic_id UUID REFERENCES procedures_clinic(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_appointments_procedure_clinic_id ON appointments(fk_procedure_clinic_id);

ALTER TABLE public.appointments
DROP COLUMN type;