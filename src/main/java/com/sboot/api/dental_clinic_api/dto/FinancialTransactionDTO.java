package com.sboot.api.dental_clinic_api.dto;

import com.sboot.api.dental_clinic_api.enums.IncomeSource;
import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.PaymentStatus;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransactionDTO {

    private TransactionType type;

    private String description;

    private BigDecimal amount;

    private LocalDate date;

    private PaymentMethod paymentMethod;

    private IncomeSource source;

    private String patientProcedureId;

    private String patientId;

    private PaymentStatus status;

    private String category;

    private String supplier;

    private String notes;
}