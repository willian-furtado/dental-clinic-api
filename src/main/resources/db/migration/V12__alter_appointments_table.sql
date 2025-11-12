-- Rename patient_name to patient_id and change type to UUID with foreign key
ALTER TABLE public.appointments
ADD COLUMN patient_id_temp UUID;

UPDATE public.appointments
SET patient_id_temp = CAST(id AS UUID)
WHERE patient_name IS NOT NULL;

ALTER TABLE public.appointments
DROP COLUMN patient_name;

ALTER TABLE public.appointments
RENAME COLUMN patient_id_temp TO patient_id;

ALTER TABLE public.appointments
ALTER COLUMN patient_id SET NOT NULL;

ALTER TABLE public.appointments
ADD CONSTRAINT fk_appointments_patient
FOREIGN KEY (patient_id) REFERENCES patients(id);

-- Update check constraint for status to uppercase
ALTER TABLE public.appointments
DROP CONSTRAINT appointments_status_check;

ALTER TABLE public.appointments
ADD CONSTRAINT appointments_status_check
CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED'));
