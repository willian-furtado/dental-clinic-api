package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.PaymentInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentInstallmentRepository extends JpaRepository<PaymentInstallment, String> {

    List<PaymentInstallment> findByPatientProcedureId(String patientProcedureId);

    @Query("SELECT pi FROM PaymentInstallment pi WHERE pi.patientProcedure.id = :patientProcedureId AND pi.status = 'OVERDUE' AND pi.dueDate < :currentDate")
    List<PaymentInstallment> findOverdueInstallmentsByPatientProcedureId(
            @Param("patientProcedureId") String patientProcedureId,
            @Param("currentDate") LocalDate currentDate
    );

    @Query("SELECT pi FROM PaymentInstallment pi WHERE pi.status = 'OVERDUE' AND pi.dueDate < :currentDate")
    List<PaymentInstallment> findOverdueInstallments(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT pi FROM PaymentInstallment pi WHERE pi.status = 'PENDING' AND pi.dueDate <= :currentDate")
    List<PaymentInstallment> findDueInstallments(@Param("currentDate") LocalDate currentDate);
}