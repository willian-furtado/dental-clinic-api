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
public class BudgetDTO {
    private String id;
    private Long code;
    private String patientId;
    private List<BudgetProcedureDTO> procedures;
    private Double discount;
    private Double total;
    private String status;
    private String createdAt;
    private String validUntil;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BudgetProcedureDTO {
        private Integer toothNumber;
        private List<String> faces;
        private String procedure;
        private Double value;
        private String notes;
    }
}

