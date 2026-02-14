package com.sboot.api.dental_clinic_api.entity;

import com.sboot.api.dental_clinic_api.enums.PaymentDiscountType;
import com.sboot.api.dental_clinic_api.enums.TreatmentPlanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "treatment_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;

    @Column(unique = true)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TreatmentPlanStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private LocalDate validUntil;

    @Column(nullable = false, length = 150)
    private String createdBy;

    @Column(nullable = false)
    private BigDecimal finalValue;

    @Column(nullable = false)
    private BigDecimal paymentDiscount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentDiscountType paymentDiscountType;

    @Column(nullable = false)
    private BigDecimal paymentDiscountAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TreatmentPlanProcedure> procedures;

    @OneToOne(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private TreatmentPlanTerms terms;

    @OneToOne(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private TreatmentPlanContract contract;

    @OneToMany(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentInstallment> paymentInstallments;
}
