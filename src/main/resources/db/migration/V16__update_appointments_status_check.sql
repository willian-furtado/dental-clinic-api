-- Update check constraint for status to include CONFIRMED
ALTER TABLE public.appointments
DROP CONSTRAINT appointments_status_check;

ALTER TABLE public.appointments
ADD CONSTRAINT appointments_status_check
CHECK (status IN ('SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED'));
