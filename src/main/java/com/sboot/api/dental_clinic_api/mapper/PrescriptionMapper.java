package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.PrescriptionDTO;
import com.sboot.api.dental_clinic_api.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PrescriptionMedicationMapper.class})
public interface PrescriptionMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "createdAt", expression = "java(prescription.getCreatedAt() != null ? prescription.getCreatedAt().toString() : null)")
    PrescriptionDTO toDto(Prescription prescription);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Prescription toEntity(PrescriptionDTO prescriptionDTO);
}
