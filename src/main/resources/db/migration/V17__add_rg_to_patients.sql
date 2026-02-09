-- Add RG column to patients table
ALTER TABLE public.patients
ADD COLUMN IF NOT EXISTS rg VARCHAR(20);
