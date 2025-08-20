package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.ProcedureClinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedureClinicRepository extends JpaRepository<ProcedureClinic, String> {

    Page<ProcedureClinic> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category, Pageable pageable);
}