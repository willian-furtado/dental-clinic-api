ALTER TABLE public.patients
ALTER COLUMN birth_date TYPE VARCHAR(10)
USING TO_CHAR(birth_date, 'YYYY-MM-DD');
