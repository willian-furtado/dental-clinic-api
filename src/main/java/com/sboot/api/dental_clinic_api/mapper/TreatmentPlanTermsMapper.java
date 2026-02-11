package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.TreatmentPlanTermsDTO;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlanTerms;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentPlanTermsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "treatmentPlan", ignore = true)
    TreatmentPlanTerms toEntity(TreatmentPlanTermsDTO dto);
}
