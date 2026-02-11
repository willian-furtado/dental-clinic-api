package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanProcedureDTO {
    private String id;
    private String procedureId;
    private Integer toothNumber;
    private List<String> faces;
    private String procedure;
    private BigDecimal value;
    private String notes;
}
