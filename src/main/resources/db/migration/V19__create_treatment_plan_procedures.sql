CREATE TABLE treatment_plan_procedures (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    treatment_plan_id UUID NOT NULL REFERENCES treatment_plans(id) ON DELETE CASCADE,
    tooth_number INT NOT NULL,
    faces TEXT,
    procedure_name VARCHAR(200) NOT NULL,
    value NUMERIC(15,2) NOT NULL,
    notes TEXT,
    is_general_procedure BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_treatment_plan_procedures_treatment_plan_id ON treatment_plan_procedures(treatment_plan_id);
