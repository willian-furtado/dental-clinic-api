package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.PatientDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientDocumentRepository extends JpaRepository<PatientDocument, String> {
    Optional<PatientDocument> findByIdAndPatientId(String documentId, String patientId);
}
