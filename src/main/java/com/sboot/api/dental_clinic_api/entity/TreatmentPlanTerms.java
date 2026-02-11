package com.sboot.api.dental_clinic_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "treatment_plan_terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private Boolean signed = false;

    private Instant signedAt;

    @Column(length = 200)
    private String signedBy;

    @Column(columnDefinition = "TEXT")
    private String termsContent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_plan_id", nullable = false)
    private TreatmentPlan treatmentPlan;
}
