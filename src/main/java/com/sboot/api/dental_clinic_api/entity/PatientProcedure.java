package com.sboot.api.dental_clinic_api.entity;

import com.sboot.api.dental_clinic_api.enums.PatientProcedureStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_procedures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_plan_id")
    private TreatmentPlan treatmentPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_clinic_id", nullable = false)
    private ProcedureClinic procedureClinic;

    @Column(name = "tooth_number")
    private Integer toothNumber;

    @Column(name = "faces")
    private String faces;

    @Column(name = "value", precision = 15, scale = 2, nullable = false)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PatientProcedureStatus status;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "origin")
    private String origin;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}