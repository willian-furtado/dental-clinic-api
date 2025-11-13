package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.PatientDTO;
import com.sboot.api.dental_clinic_api.entity.Patient;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        uses = {DocumentMapper.class, AnamnesisResponseMapper.class, AddressMapper.class})
public interface PatientMapper {
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "selectedTemplate", source = "anamnesisHistory.template.id")
    @Mapping(target = "guardian.name", source = "guardianName")
    @Mapping(target = "guardian.cpf", source = "guardianCpf")
    @Mapping(target = "guardian.phone", source = "guardianPhone")
    @Mapping(target = "guardian.email", source = "guardianEmail")
    @Mapping(target = "guardian.relationship", source = "guardianRelationship")
    PatientDTO toDto(Patient patient);

    @Mapping(target = "documents", source = "documents")
    @Mapping(target = "anamnesisResponses", source = "anamnesisResponses", ignore = true)
    @Mapping(target = "photo", source = "photo", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lastConsultationDate", expression = "java(patientDTO.getLastConsultationDate() != null ? java.time.LocalDateTime.parse(patientDTO.getLastConsultationDate()) : null)")
    @Mapping(target = "guardianName", source = "guardian.name")
    @Mapping(target = "guardianCpf", source = "guardian.cpf")
    @Mapping(target = "guardianPhone", source = "guardian.phone")
    @Mapping(target = "guardianEmail", source = "guardian.email")
    @Mapping(target = "guardianRelationship", source = "guardian.relationship")
    Patient toEntity(PatientDTO patientDTO);

    @AfterMapping
    default void mapPhoto(Patient patient, @MappingTarget PatientDTO dto) {
        if (patient.getPhoto() != null) {
            String base64 = java.util.Base64.getEncoder().encodeToString(patient.getPhoto());
            dto.setPhoto(base64);
        }
    }
}
