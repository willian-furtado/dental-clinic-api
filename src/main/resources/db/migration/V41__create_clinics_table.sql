CREATE TABLE clinic_addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cep VARCHAR(9),
    street VARCHAR(200),
    number VARCHAR(20),
    complement VARCHAR(100),
    neighborhood VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(2)
);

CREATE TABLE clinics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    cnpj VARCHAR(18) UNIQUE,
    address_id UUID REFERENCES clinic_addresses(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
