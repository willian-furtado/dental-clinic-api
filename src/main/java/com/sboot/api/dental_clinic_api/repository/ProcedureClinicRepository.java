package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.ProcedureClinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProcedureClinicRepository extends JpaRepository<ProcedureClinic, String> {

    @Query(value = "SELECT * FROM procedures_clinic WHERE (:search IS NULL OR :search = '' OR LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(category) LIKE LOWER(CONCAT('%', :search, '%')) )", nativeQuery = true)
    Page<ProcedureClinic> findAllProcedures(Pageable pageable, String search);

    List<ProcedureClinic> findByCategoryNot(String category);

    @Query("SELECT COUNT(p) FROM ProcedureClinic p")
    Long countAllProcedures();

    @Query("SELECT COUNT(p) FROM ProcedureClinic p WHERE p.isActive = true")
    Long countActiveProcedures();

    @Query("SELECT COALESCE(AVG(p.basePrice), 0) FROM ProcedureClinic p")
    BigDecimal getAveragePrice();

    List<ProcedureClinic> findByRequiresBudgetTrue();
}
