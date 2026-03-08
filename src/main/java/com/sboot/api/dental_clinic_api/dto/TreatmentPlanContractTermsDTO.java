package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanContractTermsDTO {

    private TreatmentPlanTermsDTO terms;
    private TreatmentPlanContractDTO contract;
}
