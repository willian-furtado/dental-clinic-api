CREATE TABLE public.patient_anamnesis (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    patient_id uuid NOT NULL REFERENCES public.patients(id) ON DELETE CASCADE,
    template_id uuid NOT NULL REFERENCES public.anamnesis_templates(id),
    created_at timestamp DEFAULT now()
);