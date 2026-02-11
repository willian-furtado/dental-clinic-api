package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentPlanContractDTO {
    private String id;
    private Boolean signed;
    private Instant signedAt;
    private String signedBy;
    private String contractContent;
    private Boolean contractorSignature;
    private Instant contractorSignedAt;
    private String contractorSignedBy;
    private String contractorSignatureImage;
    private String signatureImage;
}
