package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.TreatmentPlanContractDTO;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlanContract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentPlanContractMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "treatmentPlan", ignore = true)
    TreatmentPlanContract toEntity(TreatmentPlanContractDTO dto);
}
