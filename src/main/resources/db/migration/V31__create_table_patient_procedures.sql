CREATE TABLE public.patient_procedures (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    patient_id uuid NOT NULL,
    treatment_plan_id uuid NULL,
    procedure_clinic_id uuid NOT NULL,
    tooth_number int4 NULL,
    faces text NULL,
    value numeric(15,2) NOT NULL,
    status varchar(20) NOT NULL,
    scheduled_date date NULL,
    notes text NULL,
    created_at timestamp DEFAULT now(),

    CONSTRAINT fk_patient_procedure_patient
        FOREIGN KEY (patient_id)
        REFERENCES public.patients(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_patient_procedure_plan
        FOREIGN KEY (treatment_plan_id)
        REFERENCES public.treatment_plans(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_patient_procedure_procedure
        FOREIGN KEY (procedure_clinic_id)
        REFERENCES public.procedures_clinic(id)
);
