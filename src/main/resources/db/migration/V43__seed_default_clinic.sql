-- Inserir clínica padrão
INSERT INTO clinics (id, name, phone, email, created_at, updated_at)
VALUES (
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    'Clínica',
    '(11) 0000-0000',
    'contato@clinica.com',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

UPDATE users SET clinic_id = 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11' WHERE username = 'admin';