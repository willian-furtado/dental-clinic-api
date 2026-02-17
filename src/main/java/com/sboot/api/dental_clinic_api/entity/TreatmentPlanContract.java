package com.sboot.api.dental_clinic_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "treatment_plan_contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanContract {

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
    private String contractContent;

    @Column()
    private Boolean contractorSignature = false;

    private Instant contractorSignedAt;

    @Column(length = 200)
    private String contractorSignedBy;

    @Column(columnDefinition = "TEXT")
    private String contractorSignatureImage;

    @Column(columnDefinition = "TEXT")
    private String signatureImage;

    @Column(length = 500)
    private String paymentConditions;

    @Column(length = 100)
    private String missedAppointmentFee;

    @Column(length = 50)
    private String minimumHours;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_plan_id", nullable = false)
    private TreatmentPlan treatmentPlan;
}
