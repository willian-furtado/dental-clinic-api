package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.AnamnesisQuestionDTO;
import com.sboot.api.dental_clinic_api.dto.AnamnesisTemplateDTO;
import com.sboot.api.dental_clinic_api.dto.ConditionalTextDTO;
import com.sboot.api.dental_clinic_api.entity.AnamnesisQuestion;
import com.sboot.api.dental_clinic_api.entity.AnamnesisTemplate;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnamnesisTemplateMapper {

    AnamnesisTemplate toEntity(AnamnesisTemplateDTO dto);

    AnamnesisTemplateDTO toDto(AnamnesisTemplate entity);

    @Mapping(target = "triggerValue", source = "conditionalText.triggerValue")
    @Mapping(target = "label", source = "conditionalText.label")
    @Mapping(target = "placeholder", source = "conditionalText.placeholder")
    AnamnesisQuestion toEntityAnamnesisQuestion(AnamnesisQuestionDTO dto);

    @Mapping(target = "conditionalText.triggerValue", source = "triggerValue")
    @Mapping(target = "conditionalText.label", source = "label")
    @Mapping(target = "conditionalText.placeholder", source = "placeholder")
    AnamnesisQuestionDTO toDtoAnamnesisQuestionDTO(AnamnesisQuestion entity);

    List<AnamnesisQuestionDTO> questionsToDto(List<AnamnesisQuestion> questions);
    List<AnamnesisQuestion> questionsToEntity(List<AnamnesisQuestionDTO> dtos);

    @AfterMapping
    default void handleConditionalTextNull(AnamnesisQuestion entity, @MappingTarget AnamnesisQuestionDTO dto) {
        if (dto.getConditionalText() != null) {
            ConditionalTextDTO ct = dto.getConditionalText();
            if (ct.getTriggerValue() == null && ct.getLabel() == null && ct.getPlaceholder() == null) {
                dto.setConditionalText(null);
            }
        }
    }

    @AfterMapping
    default void handleConditionalTextNullDto(AnamnesisQuestionDTO dto, @MappingTarget AnamnesisQuestion entity) {
        if (dto.getConditionalText() == null) {
            entity.setTriggerValue(null);
            entity.setLabel(null);
            entity.setPlaceholder(null);
        }
    }
}

