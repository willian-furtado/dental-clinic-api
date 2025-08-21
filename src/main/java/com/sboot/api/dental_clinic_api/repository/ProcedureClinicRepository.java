package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.ProcedureClinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ProcedureClinicRepository extends JpaRepository<ProcedureClinic, String> {

    Page<ProcedureClinic> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category, Pageable pageable);

    @Query("SELECT COUNT(p) FROM ProcedureClinic p")
    Long countAllProcedures();

    @Query("SELECT COUNT(p) FROM ProcedureClinic p WHERE p.isActive = true")
    Long countActiveProcedures();

    @Query("SELECT COALESCE(AVG(p.basePrice), 0) FROM ProcedureClinic p")
    BigDecimal getAveragePrice();
}