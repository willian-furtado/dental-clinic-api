package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.PatientProcedureDTO;
import com.sboot.api.dental_clinic_api.dto.PatientProcedureResponseDTO;
import com.sboot.api.dental_clinic_api.entity.PatientProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, TreatmentPlanMapper.class})
public interface PatientProcedureMapper {
    

    @Mapping(source = "procedureClinic.name", target = "procedureName")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "treatmentPlanId", source = "treatmentPlan.id")
    @Mapping(target = "procedureClinicId", source = "procedureClinic.id")
    @Mapping(target = "appointmentId", source = "appointment.id")
    PatientProcedureDTO toDTO(PatientProcedure patientProcedure);

    @Mapping(target = "patient", source = "patient")
    @Mapping(target = "patient.documents", ignore = true)
    @Mapping(target = "patient.photo", ignore = true)
    @Mapping(target = "treatmentPlan", source = "treatmentPlan")
    @Mapping(target = "procedureClinic", source = "procedureClinic")
    @Mapping(target = "appointment", source = "appointment")
    PatientProcedureResponseDTO toResponseDTO(PatientProcedure patientProcedure);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "treatmentPlan", ignore = true)
    @Mapping(target = "procedureClinic", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PatientProcedure toEntity(PatientProcedureDTO dto);
}
