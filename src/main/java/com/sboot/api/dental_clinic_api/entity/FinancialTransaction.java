package com.sboot.api.dental_clinic_api.entity;

import com.sboot.api.dental_clinic_api.enums.IncomeSource;
import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.PaymentStatus;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private IncomeSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_procedure_id")
    private PatientProcedure patientProcedure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus status;

    @Column(length = 100)
    private String category;

    @Column(length = 255)
    private String supplier;

    @Column(columnDefinition = "TEXT")
    private String notes;
}