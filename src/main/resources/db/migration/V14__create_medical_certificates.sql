CREATE TABLE medical_certificates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id UUID NOT NULL REFERENCES patients(id),
    certificate_type VARCHAR(10) NOT NULL CHECK (certificate_type IN ('DAYS', 'HOURS')),
    start_date DATE,
    end_date DATE,
    start_time VARCHAR(10),
    end_time VARCHAR(10),
    reason TEXT NOT NULL,
    observations TEXT,
    cid_code VARCHAR(10),
    cid_description VARCHAR(255),
    doctor_name VARCHAR(255) NOT NULL,
    cro VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
