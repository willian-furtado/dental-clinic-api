package com.sboot.api.dental_clinic_api.dto;

import com.sboot.api.dental_clinic_api.enums.PaymentDiscountType;
import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.TreatmentPlanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanResponseDTO {
    private String id;
    private String code;
    private String patientId;
    private String patientName;
    private List<TreatmentPlanProcedureDTO> procedures;
    private BigDecimal subtotal;
    private BigDecimal total;
    private TreatmentPlanStatus status;
    private String notes;
    private LocalDate validUntil;
    private String createdBy;
    private PaymentMethod paymentMethod;
    private BigDecimal finalValue;
    private BigDecimal paymentDiscount;
    private PaymentDiscountType paymentDiscountType;
    private BigDecimal paymentDiscountAmount;
    private TreatmentPlanTermsDTO terms;
    private TreatmentPlanContractDTO contract;
    private List<PaymentInstallmentDTO> paymentInstallments;
    private LocalDateTime createdAt;
}
