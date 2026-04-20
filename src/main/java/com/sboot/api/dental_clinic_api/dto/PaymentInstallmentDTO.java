package com.sboot.api.dental_clinic_api.dto;

import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInstallmentDTO {

    private String id;
    private String patientProcedureId;
    private LocalDate dueDate;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private Integer installmentNumber;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}