package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalCertificateDTO {

    private String id;
    private String patientId;
    private String certificateType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String startTime;
    private String endTime;
    private String reason;
    private String observations;
    private String cidCode;
    private String cidDescription;
    private String doctorName;
    private String cro;
    private String createdAt;
}
