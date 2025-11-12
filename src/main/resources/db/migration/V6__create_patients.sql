CREATE TABLE public.patients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255) NOT NULL,
    cpf varchar(14) UNIQUE NOT NULL,
    phone varchar(20),
    email varchar(150),
    birth_date date NOT NULL,
    photo bytea,
    created_at TIMESTAMP DEFAULT NOW(),
    guardian_name varchar(255),
    guardian_cpf varchar(14),
    guardian_phone varchar(20),
    guardian_email varchar(150),
    guardian_relationship varchar(50)
);