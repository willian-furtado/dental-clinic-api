-- Adiciona novos campos ao contrato do plano de tratamento
ALTER TABLE treatment_plan_contract 
ADD COLUMN payment_conditions VARCHAR(500),
ADD COLUMN missed_appointment_fee VARCHAR(100),
ADD COLUMN minimum_hours VARCHAR(50);
