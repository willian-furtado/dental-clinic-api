package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.TreatmentPlanProcedureDTO;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlanProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FacesMapper.class})
public interface TreatmentPlanProcedureMapper {

    @Mapping(target = "faces", qualifiedByName = "stringToList")
    @Mapping(target = "procedureId", expression = "java(entity.getProcedureClinic() != null ? entity.getProcedureClinic().getId() : null)")
    TreatmentPlanProcedureDTO toDTO(TreatmentPlanProcedure entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "procedureClinic.id", source = "procedureId")
    @Mapping(target = "treatmentPlan", ignore = true)
    @Mapping(target = "faces", qualifiedByName = "listToString")
    TreatmentPlanProcedure toEntity(TreatmentPlanProcedureDTO dto);

}
