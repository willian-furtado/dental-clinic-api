package com.sboot.api.dental_clinic_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "treatment_plan_procedures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private Integer toothNumber;

    @Column(columnDefinition = "TEXT")
    private String faces;

    @Column(name = "procedure_name", nullable = false, length = 200)
    private String procedure;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_plan_id", nullable = false)
    private TreatmentPlan treatmentPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_clinic_id")
    private ProcedureClinic procedureClinic;
}
