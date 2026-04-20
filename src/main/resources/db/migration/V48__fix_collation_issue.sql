-- Corrigir problema de collation entre patient_procedures e financial_transactions
-- Este problema causa erro "cache lookup failed for collation 0"

-- 1. Backup dos dados de financial_transactions
CREATE TEMP TABLE financial_transactions_backup AS 
SELECT * FROM financial_transactions;

-- 2. Backup dos dados de patient_procedures
CREATE TEMP TABLE patient_procedures_backup AS 
SELECT * FROM patient_procedures;

-- 3. Remover TODAS as foreign keys que referenciam patient_procedures
DO $$ 
DECLARE 
    r RECORD;
BEGIN
    -- Remove todas as foreign keys que referenciam patient_procedures
    FOR r IN (
        SELECT tc.table_name, tc.constraint_name
        FROM information_schema.table_constraints tc
        JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name
        JOIN information_schema.constraint_column_usage ccu ON ccu.constraint_name = tc.constraint_name
        WHERE tc.constraint_type = 'FOREIGN KEY' 
        AND ccu.table_name = 'patient_procedures'
    ) LOOP
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' DROP CONSTRAINT IF EXISTS ' || quote_ident(r.constraint_name) || ' CASCADE';
    END LOOP;
END $$;

-- 4. Remover TODAS as foreign keys que referenciam financial_transactions
DO $$ 
DECLARE 
    r RECORD;
BEGIN
    FOR r IN (
        SELECT tc.table_name, tc.constraint_name
        FROM information_schema.table_constraints tc
        JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name
        JOIN information_schema.constraint_column_usage ccu ON ccu.constraint_name = tc.constraint_name
        WHERE tc.constraint_type = 'FOREIGN KEY' 
        AND ccu.table_name = 'financial_transactions'
    ) LOOP
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' DROP CONSTRAINT IF EXISTS ' || quote_ident(r.constraint_name) || ' CASCADE';
    END LOOP;
END $$;

-- 5. Dropar tabela financial_transactions
DROP TABLE IF EXISTS financial_transactions CASCADE;

-- 6. Dropar tabela patient_procedures
DROP TABLE IF EXISTS patient_procedures CASCADE;

-- 7. Recriar tabela patient_procedures com collation correta
CREATE TABLE patient_procedures (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id UUID NOT NULL,
    treatment_plan_id UUID NULL,
    procedure_clinic_id UUID NOT NULL,
    tooth_number INT4 NULL,
    faces TEXT NULL,
    value NUMERIC(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL COLLATE "C",
    scheduled_date DATE NULL,
    notes TEXT NULL,
    origin VARCHAR(50) NULL COLLATE "C",
    appointment_id UUID NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_patient_procedure_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id)
        ON DELETE CASCADE,
    
    CONSTRAINT fk_patient_procedure_plan
        FOREIGN KEY (treatment_plan_id)
        REFERENCES treatment_plans(id)
        ON DELETE SET NULL,
    
    CONSTRAINT fk_patient_procedure_procedure
        FOREIGN KEY (procedure_clinic_id)
        REFERENCES procedures_clinic(id),
    
    CONSTRAINT fk_patient_procedures_appointment
        FOREIGN KEY (appointment_id)
        REFERENCES appointments(id)
);

-- 8. Recriar tabela financial_transactions com collation correta
CREATE TABLE financial_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(25) NOT NULL COLLATE "C",
    description VARCHAR(255) NOT NULL COLLATE "C",
    amount DECIMAL(15, 2) NOT NULL,
    date DATE NOT NULL,
    payment_method VARCHAR(30) NOT NULL COLLATE "C",
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(30) COLLATE "C",
    patient_procedure_id UUID,
    patient_id UUID,
    status VARCHAR(20) COLLATE "C",
    category VARCHAR(100) COLLATE "C",
    supplier VARCHAR(255) COLLATE "C",
    notes TEXT COLLATE "C",
    
    CONSTRAINT fk_financial_transaction_patient_procedure 
        FOREIGN KEY (patient_procedure_id) REFERENCES patient_procedures(id) ON DELETE SET NULL,
    CONSTRAINT fk_financial_transaction_patient 
        FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE SET NULL
);

-- 9. Criar índices
CREATE INDEX idx_patient_procedures_patient_id ON patient_procedures(patient_id);
CREATE INDEX idx_patient_procedures_treatment_plan_id ON patient_procedures(treatment_plan_id);
CREATE INDEX idx_patient_procedures_procedure_clinic_id ON patient_procedures(procedure_clinic_id);
CREATE INDEX idx_patient_procedures_appointment_id ON patient_procedures(appointment_id);

CREATE INDEX idx_financial_transactions_type ON financial_transactions(type);
CREATE INDEX idx_financial_transactions_date ON financial_transactions(date);
CREATE INDEX idx_financial_transactions_patient_id ON financial_transactions(patient_id);
CREATE INDEX idx_financial_transactions_patient_procedure_id ON financial_transactions(patient_procedure_id);

-- 10. Restaurar dados de patient_procedures
INSERT INTO patient_procedures (
    id, patient_id, treatment_plan_id, procedure_clinic_id,
    tooth_number, faces, value, status, scheduled_date, notes,
    origin, appointment_id, created_at
)
SELECT 
    id, patient_id, treatment_plan_id, procedure_clinic_id,
    tooth_number, faces, value, status, scheduled_date, notes,
    origin, appointment_id, created_at
FROM patient_procedures_backup;

-- 11. Restaurar dados de financial_transactions
INSERT INTO financial_transactions (
    id, type, description, amount, date, payment_method,
    created_at, source, patient_procedure_id, patient_id,
    status, category, supplier, notes
)
SELECT 
    id, type, description, amount, date, payment_method,
    created_at, source, 
    patient_procedure_id::UUID,
    patient_id::UUID,
    status, category, supplier, notes
FROM financial_transactions_backup;

-- 12. Limpar tabelas temporárias
DROP TABLE financial_transactions_backup;
DROP TABLE patient_procedures_backup;