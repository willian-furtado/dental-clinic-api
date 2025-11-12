package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.PrescriptionMedicationDTO;
import com.sboot.api.dental_clinic_api.entity.PrescriptionMedication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionMedicationMapper {

    PrescriptionMedicationDTO toDto(PrescriptionMedication prescriptionMedication);

    PrescriptionMedication toEntity(PrescriptionMedicationDTO prescriptionMedicationDTO);
}
