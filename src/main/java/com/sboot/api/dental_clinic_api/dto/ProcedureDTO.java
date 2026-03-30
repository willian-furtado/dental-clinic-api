package com.sboot.api.dental_clinic_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureDTO {
    private String id;
    private String patientId;
    private String procedureType;
    private String description;
    private String date;
    private String dentist;
    private String status;
    private Double cost;
    private String notes;
    private Integer toothNumber;
    private List<String> faces;
}

