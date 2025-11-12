package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDocumentDTO {

    private String id;

    private String name;

    private Long size;

    private String type;

    private String uploadDate;

    private String byteData;
}
