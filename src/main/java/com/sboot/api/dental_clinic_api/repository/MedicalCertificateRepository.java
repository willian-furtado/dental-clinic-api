package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.MedicalCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalCertificateRepository extends JpaRepository<MedicalCertificate, String> {

    List<MedicalCertificate> findByPatientId(String patientId);
}
