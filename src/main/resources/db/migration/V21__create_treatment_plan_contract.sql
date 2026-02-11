CREATE TABLE treatment_plan_contract (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    treatment_plan_id UUID NOT NULL REFERENCES treatment_plans(id) ON DELETE CASCADE,
    signed BOOLEAN NOT NULL DEFAULT FALSE,
    signed_at TIMESTAMP WITH TIME ZONE,
    signed_by VARCHAR(200),
    contract_content TEXT,
    contractor_signature BOOLEAN NOT NULL DEFAULT FALSE,
    contractor_signed_at TIMESTAMP WITH TIME ZONE,
    contractor_signed_by VARCHAR(200),
    contractor_signature_image TEXT,
    signature_image TEXT
);

CREATE INDEX idx_treatment_plan_contract_treatment_plan_id ON treatment_plan_contract(treatment_plan_id);
