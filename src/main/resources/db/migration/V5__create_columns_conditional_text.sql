
ALTER TABLE anamnesis_questions
ADD COLUMN conditional_trigger_value VARCHAR(255),
ADD COLUMN conditional_label VARCHAR(255),
ADD COLUMN conditional_placeholder VARCHAR(255);
