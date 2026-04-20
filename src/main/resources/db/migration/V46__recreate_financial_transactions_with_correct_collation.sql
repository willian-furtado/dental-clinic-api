DROP TABLE IF EXISTS financial_transactions CASCADE;

CREATE TABLE financial_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(25) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    date DATE NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(30),
    patient_procedure_id VARCHAR(255),
    patient_id VARCHAR(255),
    status VARCHAR(20),
    category VARCHAR(100),
    supplier VARCHAR(255),
    notes TEXT,
    
    CONSTRAINT fk_financial_transaction_patient_procedure 
        FOREIGN KEY (patient_procedure_id) REFERENCES patient_procedures(id) ON DELETE SET NULL,
    CONSTRAINT fk_financial_transaction_patient 
        FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE SET NULL
);

CREATE INDEX idx_financial_transactions_type ON financial_transactions(type);
CREATE INDEX idx_financial_transactions_date ON financial_transactions(date);
CREATE INDEX idx_financial_transactions_patient_id ON financial_transactions(patient_id);
CREATE INDEX idx_financial_transactions_patient_procedure_id ON financial_transactions(patient_procedure_id);