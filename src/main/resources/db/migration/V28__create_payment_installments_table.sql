-- Create payment_installments table
CREATE TABLE payment_installments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    treatment_plan_id UUID NOT NULL REFERENCES treatment_plans(id) ON DELETE CASCADE,
    due_date DATE NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    installment_number INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for payment_installments table
CREATE INDEX idx_payment_installments_treatment_plan_id ON payment_installments(treatment_plan_id);
CREATE INDEX idx_payment_installments_status ON payment_installments(status);
CREATE INDEX idx_payment_installments_due_date ON payment_installments(due_date);
CREATE INDEX idx_payment_installments_installment_number ON payment_installments(installment_number);