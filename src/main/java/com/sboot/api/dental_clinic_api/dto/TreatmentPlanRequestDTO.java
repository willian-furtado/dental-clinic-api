package com.sboot.api.dental_clinic_api.dto;

import com.sboot.api.dental_clinic_api.enums.PaymentDiscountType;
import com.sboot.api.dental_clinic_api.enums.TreatmentPlanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanRequestDTO {
    private Long code;
    private String patientId;
    private String patientName;
    private List<TreatmentPlanProcedureDTO> procedures;
    private BigDecimal subtotal;
    private BigDecimal total;
    private TreatmentPlanStatus status;
    private String notes;
    private LocalDate validUntil;
    private String createdBy;
    private BigDecimal finalValue;
    private BigDecimal paymentDiscount;
    private PaymentDiscountType paymentDiscountType;
    private BigDecimal paymentDiscountAmount;
    private TreatmentPlanTermsDTO terms;
    private TreatmentPlanContractDTO contract;
}
