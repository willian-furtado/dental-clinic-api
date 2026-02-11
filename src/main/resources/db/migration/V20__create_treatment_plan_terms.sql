CREATE TABLE treatment_plan_terms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    treatment_plan_id UUID NOT NULL REFERENCES treatment_plans(id) ON DELETE CASCADE,
    signed BOOLEAN NOT NULL DEFAULT FALSE,
    signed_at TIMESTAMP WITH TIME ZONE,
    signed_by VARCHAR(200),
    terms_content TEXT
);

CREATE INDEX idx_treatment_plan_terms_treatment_plan_id ON treatment_plan_terms(treatment_plan_id);
