CREATE TABLE public.anamnesis_responses (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    patient_id uuid NOT NULL REFERENCES public.patients(id) ON DELETE CASCADE,
    question_id uuid NOT NULL REFERENCES public.anamnesis_questions(id) ON DELETE CASCADE,
    selected_option_id uuid NULL REFERENCES public.anamnesis_question_options(id),
    value text NULL,
    extra_text text NULL,
    created_at timestamp DEFAULT now()
);