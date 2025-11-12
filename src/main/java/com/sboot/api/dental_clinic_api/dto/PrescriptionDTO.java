package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDTO {

    private String id;

    private String patientId;

    private List<PrescriptionMedicationDTO> medications;

    private String observations;

    private String doctorName;

    private String cro;

    private String createdAt;
}
