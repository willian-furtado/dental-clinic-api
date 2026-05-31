package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.RecurringExpenseRequestDTO;
import com.sboot.api.dental_clinic_api.dto.RecurringExpenseResponseDTO;
import com.sboot.api.dental_clinic_api.entity.RecurringExpense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RecurringExpenseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RecurringExpense toEntity(RecurringExpenseRequestDTO dto);

    RecurringExpenseResponseDTO toResponseDTO(RecurringExpense entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(RecurringExpenseRequestDTO dto, @MappingTarget RecurringExpense entity);
}