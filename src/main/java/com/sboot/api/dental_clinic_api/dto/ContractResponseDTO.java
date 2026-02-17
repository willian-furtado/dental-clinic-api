package com.sboot.api.dental_clinic_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponseDTO {

    private String template;
    
    private EditableFields editableFields;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EditableFields {
        private String paymentConditions;
        private String missedAppointmentFee;
        private String minimumHours;
    }
}
