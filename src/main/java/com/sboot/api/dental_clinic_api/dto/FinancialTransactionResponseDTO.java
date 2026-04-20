package com.sboot.api.dental_clinic_api.dto;

import com.sboot.api.dental_clinic_api.enums.IncomeSource;
import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.PaymentStatus;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransactionResponseDTO {

    private String id;

    private TransactionType type;

    private String description;

    private BigDecimal amount;

    private LocalDate date;

    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    private IncomeSource source;

    private String patientProcedureId;

    private String patientId;

    private String patientName;

    private PaymentStatus status;

    private String category;

    private String supplier;

    private String notes;
}