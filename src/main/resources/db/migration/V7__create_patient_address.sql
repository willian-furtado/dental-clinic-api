CREATE TABLE public.patient_addresses (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    patient_id uuid NOT NULL REFERENCES public.patients(id) ON DELETE CASCADE,
    street varchar(255),
    number varchar(50),
    complement varchar(255),
    neighborhood varchar(255),
    city varchar(255),
    state varchar(50),
    zip_code varchar(20)
);

CREATE TABLE public.patient_documents (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    patient_id uuid NOT NULL REFERENCES public.patients(id) ON DELETE CASCADE,
    name varchar(255) NOT NULL,
    size bigint,
    type varchar(100),
    upload_date timestamp DEFAULT now(),
    byte bytea
);