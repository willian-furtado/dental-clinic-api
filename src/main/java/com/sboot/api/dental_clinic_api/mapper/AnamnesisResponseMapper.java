package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.AnamnesisResponseDTO;
import com.sboot.api.dental_clinic_api.entity.AnamnesisResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnamnesisResponseMapper {

    @Mapping(target = "question.id", source = "questionId")
    @Mapping(target = "selectedOption.id", source = "selectedOptionId")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    AnamnesisResponse toEntity(AnamnesisResponseDTO dto);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "selectedOptionId", source = "selectedOption.id")
    AnamnesisResponseDTO toDto(AnamnesisResponse entity);
}