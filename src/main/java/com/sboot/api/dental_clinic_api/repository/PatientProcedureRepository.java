package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.PatientProcedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientProcedureRepository extends JpaRepository<PatientProcedure, String> {

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.patient.id = :patientId")
    List<PatientProcedure> findByPatientId(@Param("patientId") String patientId);

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.treatmentPlan.id = :treatmentPlanId")
    List<PatientProcedure> findByTreatmentPlanId(@Param("treatmentPlanId") String treatmentPlanId);

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.procedureClinic.id = :procedureClinicId")
    List<PatientProcedure> findByProcedureClinicId(@Param("procedureClinicId") String procedureClinicId);

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.patient.id = :patientId AND pp.status = :status")
    List<PatientProcedure> findByPatientIdAndStatus(@Param("patientId") String patientId, @Param("status") String status);

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.treatmentPlan.id = :treatmentPlanId AND pp.status = :status")
    List<PatientProcedure> findByTreatmentPlanIdAndStatus(@Param("treatmentPlanId") String treatmentPlanId, @Param("status") String status);

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.procedureClinic.id = :procedureClinicId AND pp.status = :status")
    List<PatientProcedure> findByProcedureClinicIdAndStatus(@Param("procedureClinicId") String procedureClinicId, @Param("status") String status);

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.patient.id = :patientId AND pp.scheduledDate = :scheduledDate")
    List<PatientProcedure> findByPatientIdAndScheduledDate(@Param("patientId") String patientId, @Param("scheduledDate") java.time.LocalDate scheduledDate);

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.treatmentPlan.id = :treatmentPlanId AND pp.scheduledDate = :scheduledDate")
    List<PatientProcedure> findByTreatmentPlanIdAndScheduledDate(@Param("treatmentPlanId") String treatmentPlanId, @Param("scheduledDate") java.time.LocalDate scheduledDate);

    @Query("SELECT pp FROM PatientProcedure pp WHERE pp.procedureClinic.id = :procedureClinicId AND pp.scheduledDate = :scheduledDate")
    List<PatientProcedure> findByProcedureClinicIdAndScheduledDate(@Param("procedureClinicId") String procedureClinicId, @Param("scheduledDate") java.time.LocalDate scheduledDate);

    @Query("SELECT pp FROM PatientProcedure pp " +
            "WHERE (:search IS NULL OR :search = '' OR " +
            "LOWER(pp.procedureClinic.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(pp.patient.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:patientId IS NULL OR pp.patient.id = :patientId) " +
            "AND (:budgetId IS NULL OR pp.treatmentPlan.id = :budgetId)")
    Page<PatientProcedure> findAllByFilters(@Param("search") String search,
                                            @Param("patientId") String patientId,
                                            @Param("budgetId") String budgetId,
                                            Pageable pageable);
}
