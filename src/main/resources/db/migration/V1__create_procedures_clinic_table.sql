CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE procedures_clinic (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    base_price NUMERIC(15,2) NOT NULL,
    duration INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL
);

CREATE CAST (character varying AS uuid)
	WITH INOUT
	AS IMPLICIT;
