-- Remove a FK antiga e recria com ON DELETE CASCADE
ALTER TABLE financial_transactions 
DROP CONSTRAINT IF EXISTS fk_financial_transaction_patient_procedure;

ALTER TABLE financial_transactions 
ADD CONSTRAINT fk_financial_transaction_patient_procedure 
FOREIGN KEY (patient_procedure_id) 
REFERENCES patient_procedures(id) 
ON DELETE CASCADE;