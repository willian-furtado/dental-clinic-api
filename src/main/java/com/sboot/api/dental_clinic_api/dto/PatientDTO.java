package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {

    private String id;

    private String name;

    private String cpf;

    private String phone;

    private String email;

    private String birthDate;

    private String photo;

    private String createdAt;

    private String lastConsultationDate;

    private GuardianDTO guardian;

    private PatientAddressDTO address;

    private List<PatientDocumentDTO> documents;

    private List<AnamnesisResponseDTO> anamnesisResponses;

    private String selectedTemplate;

}
