package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.FinancialTransactionDTO;
import com.sboot.api.dental_clinic_api.dto.FinancialTransactionResponseDTO;
import com.sboot.api.dental_clinic_api.entity.FinancialTransaction;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.PatientProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FinancialTransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "patientProcedure", ignore = true)
    FinancialTransaction toEntity(FinancialTransactionDTO dto);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientProcedureId", source = "patientProcedure.id")
    @Mapping(target = "patientName", source = "patient.name")
    FinancialTransactionResponseDTO toResponseDTO(FinancialTransaction entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "patientProcedure", ignore = true)
    void updateEntityFromDTO(FinancialTransactionDTO dto, @MappingTarget FinancialTransaction entity);

    default void setPatientAndProcedure(FinancialTransaction entity, Patient patient, PatientProcedure patientProcedure) {
        entity.setPatient(patient);
        entity.setPatientProcedure(patientProcedure);
    }
}