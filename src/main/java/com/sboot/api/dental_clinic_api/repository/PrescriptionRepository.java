package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, String> {

    List<Prescription> findByPatientId(String patientId);
}
