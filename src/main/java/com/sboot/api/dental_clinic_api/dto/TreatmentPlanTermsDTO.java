package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanTermsDTO {
    private String id;
    private Boolean signed;
    private Instant signedAt;
    private String signedBy;
    private String termsContent;
    private String signatureImage;
}
