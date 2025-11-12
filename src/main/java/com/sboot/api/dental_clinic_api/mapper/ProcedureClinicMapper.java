package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.ProcedureClinicRequestDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureClinicResponseDTO;
import com.sboot.api.dental_clinic_api.entity.ProcedureClinic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProcedureClinicMapper {

    ProcedureClinicResponseDTO toResponse(ProcedureClinic procedure);

    ProcedureClinic toEntity(ProcedureClinicRequestDTO request);
}
