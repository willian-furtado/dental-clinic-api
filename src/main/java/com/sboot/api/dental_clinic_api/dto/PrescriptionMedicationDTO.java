package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionMedicationDTO {

    private String id;

    private String name;

    private String dosage;

    private String frequency;

    private String duration;

    private String instructions;
}
