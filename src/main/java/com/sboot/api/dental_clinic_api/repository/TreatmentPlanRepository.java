package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.TreatmentPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, String> {

    @Query("SELECT tp FROM TreatmentPlan tp " +
            "WHERE (:search IS NULL OR :search = '' " +
            "OR LOWER(tp.patient.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(tp.patient.cpf) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR CAST(tp.code AS string) LIKE CONCAT('%', :search, '%')) ")
    Page<TreatmentPlan> findAllByFilters(@Param("search") String search,
                                         Pageable pageable);

    List<TreatmentPlan> findByPatientId(String patientId);

    Page<TreatmentPlan> findByPatientId(String patientId, Pageable pageable);

    @Query(value = "SELECT NEXTVAL('treatment_plans_code_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
