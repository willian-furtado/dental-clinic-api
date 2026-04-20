-- Recriar tabela patient_procedures com collation correta
-- para resolver erro "cache lookup failed for collation 0"

-- Backup dos dados existentes
CREATE TEMP TABLE patient_procedures_backup AS 
SELECT * FROM patient_procedures;

-- Remover foreign keys que referenciam patient_procedures
ALTER TABLE financial_transactions 
DROP CONSTRAINT IF EXISTS fk_financial_transaction_patient_procedure;

ALTER TABLE patient_procedures 
DROP CONSTRAINT IF EXISTS fk_patient_procedures_appointment;

-- Dropar a tabela
DROP TABLE patient_procedures CASCADE;

-- Recriar a tabela com collation correta
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

-- Criar índices
CREATE INDEX idx_patient_procedures_patient_id ON patient_procedures(patient_id);
CREATE INDEX idx_patient_procedures_treatment_plan_id ON patient_procedures(treatment_plan_id);
CREATE INDEX idx_patient_procedures_procedure_clinic_id ON patient_procedures(procedure_clinic_id);
CREATE INDEX idx_patient_procedures_appointment_id ON patient_procedures(appointment_id);

-- Restaurar dados do backup
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

-- Recriar foreign key em financial_transactions
ALTER TABLE financial_transactions 
ADD CONSTRAINT fk_financial_transaction_patient_procedure 
FOREIGN KEY (patient_procedure_id) REFERENCES patient_procedures(id) ON DELETE SET NULL;

-- Limpar tabela temporária
DROP TABLE patient_procedures_backup;