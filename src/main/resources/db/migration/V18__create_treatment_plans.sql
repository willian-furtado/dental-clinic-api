CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE treatment_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE RESTRICT,
    subtotal NUMERIC(15,2) NOT NULL,
    total NUMERIC(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'draft',
    notes TEXT,
    valid_until DATE NOT NULL,
    created_by VARCHAR(150) NOT NULL,
    payment_method VARCHAR(30),
    installments INT NOT NULL DEFAULT 1,
    final_value NUMERIC(15,2) NOT NULL,
    payment_discount NUMERIC(15,2) NOT NULL DEFAULT 0,
    payment_discount_type VARCHAR(20),
    payment_discount_amount NUMERIC(15,2) NOT NULL DEFAULT 0,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE INDEX idx_treatment_plans_patient_id ON treatment_plans(patient_id);
CREATE INDEX idx_treatment_plans_status ON treatment_plans(status);
CREATE INDEX idx_treatment_plans_created_at ON treatment_plans(created_at);
