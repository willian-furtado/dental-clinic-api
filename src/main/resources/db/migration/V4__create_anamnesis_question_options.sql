CREATE TABLE IF NOT EXISTS anamnesis_question_options (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    question_id UUID NOT NULL REFERENCES anamnesis_questions(id) ON DELETE CASCADE,
    value VARCHAR(255) NOT NULL
);