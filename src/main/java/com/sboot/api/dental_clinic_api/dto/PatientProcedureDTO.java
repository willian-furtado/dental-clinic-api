package com.sboot.api.dental_clinic_api.dto;

import com.sboot.api.dental_clinic_api.enums.PatientProcedureStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProcedureDTO {
    
    private String id;
    private String patientId;
    private String patientName;
    private String treatmentPlanId;
    private String procedureClinicId;
    private String procedureName;
    private Integer toothNumber;
    private String faces;
    private BigDecimal value;
    private PatientProcedureStatus status;
    private LocalDate scheduledDate;
    private String notes;
    private String origin;
    private LocalDateTime createdAt;
}
