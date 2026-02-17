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
public class PatientProcedureResponseDTO {
    
    private String id;
    private PatientDTO patient;
    private TreatmentPlanResponseDTO treatmentPlan;
    private ProcedureClinicResponseDTO procedureClinic;
    private Integer toothNumber;
    private String faces;
    private BigDecimal value;
    private PatientProcedureStatus status;
    private LocalDate scheduledDate;
    private String notes;
    private String origin;
    private AppointmentDTO appointment;
    private LocalDateTime createdAt;
}