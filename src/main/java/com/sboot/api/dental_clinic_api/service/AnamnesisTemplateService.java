package com.sboot.api.dental_clinic_api.service;


import com.sboot.api.dental_clinic_api.dto.AnamnesisQuestionDTO;
import com.sboot.api.dental_clinic_api.dto.AnamnesisTemplateDTO;
import com.sboot.api.dental_clinic_api.entity.AnamnesisQuestion;
import com.sboot.api.dental_clinic_api.entity.AnamnesisQuestionOption;
import com.sboot.api.dental_clinic_api.entity.AnamnesisTemplate;
import com.sboot.api.dental_clinic_api.mapper.AnamnesisTemplateMapper;
import com.sboot.api.dental_clinic_api.repository.AnamnesisTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnamnesisTemplateService {
    private final AnamnesisTemplateRepository templateRepository;

    private final AnamnesisTemplateMapper mapper;

    public List<AnamnesisTemplateDTO> getAllTemplates(String audience, String search) {
        List<AnamnesisTemplateDTO> templates = templateRepository.findAll().stream()
                .map(template -> {
                    template.getQuestions().sort(Comparator.comparing(AnamnesisQuestion::getCreatedAt));
                    return mapper.toDto(template);
                })
                .collect(Collectors.toList());

        if (audience != null && !audience.equalsIgnoreCase("all")) {
            templates = templates.stream()
                    .filter(t -> t.getTargetAudience().equalsIgnoreCase(audience))
                    .collect(Collectors.toList());
        }

        if (search != null && !search.isEmpty()) {
            String lowerSearch = search.toLowerCase();
            templates = templates.stream()
                    .filter(t -> t.getName().toLowerCase().contains(lowerSearch)
                            || t.getDescription().toLowerCase().contains(lowerSearch))
                    .collect(Collectors.toList());
        }

        return templates;
    }

    public Optional<AnamnesisTemplateDTO> getTemplateById(String id) {
        return Optional.ofNullable(mapper.toDto(templateRepository.findByAnamneseId(id)));
    }

    public AnamnesisTemplateDTO createTemplate(AnamnesisTemplateDTO template) {
        AnamnesisTemplate entity = mapper.toEntity(template);
        entity.setCreatedAt(LocalDateTime.now());

        if (entity.getQuestions() != null) {
            entity.getQuestions().forEach(q -> {
                q.setTemplate(entity);
                q.setCreatedAt(LocalDateTime.now());
                if (q.getOptions() != null) {
                    q.getOptions().forEach(o -> o.setQuestion(q));
                }
            });
        }
        return mapper.toDto(templateRepository.save(entity));
    }

    @Transactional
    public Optional<AnamnesisTemplate> updateTemplate(String id, AnamnesisTemplateDTO updatedTemplateDTO) {
        return templateRepository.findById(id).map(template -> {
            template.setName(updatedTemplateDTO.getName());
            template.setDescription(updatedTemplateDTO.getDescription());
            template.setTargetAudience(updatedTemplateDTO.getTargetAudience());
            template.setIsActive(updatedTemplateDTO.getIsActive());
            template.setUpdatedAt(LocalDateTime.now());

            if (updatedTemplateDTO.getQuestions() != null) {
                List<AnamnesisQuestion> existingQuestions = template.getQuestions();

                for (AnamnesisQuestionDTO qDto : updatedTemplateDTO.getQuestions()) {
                    if (qDto.getId() != null) {
                        existingQuestions.stream()
                                .filter(q -> q.getId().equals(qDto.getId()))
                                .findFirst()
                                .ifPresent(q -> {
                                    q.setText(qDto.getText());
                                    q.setType(qDto.getType());
                                    q.setUpdatedAt(LocalDateTime.now());
                                    q.setRequired(qDto.getRequired());
                                    q.setCategory(qDto.getCategory());

                                    if (qDto.getConditionalText() != null) {
                                        q.setTriggerValue(qDto.getConditionalText().getTriggerValue());
                                        q.setPlaceholder(qDto.getConditionalText().getPlaceholder());
                                        q.setLabel(qDto.getConditionalText().getLabel());
                                    }

                                    if (qDto.getOptions() != null) {
                                        qDto.getOptions().forEach(optDto -> {
                                            if (optDto.getId() == null) {
                                                AnamnesisQuestionOption newOption = new AnamnesisQuestionOption();
                                                newOption.setValue(optDto.getValue());
                                                newOption.setQuestion(q);
                                                q.getOptions().add(newOption);
                                            }
                                        });
                                    }
                                });
                    } else {
                        AnamnesisQuestion newQuestion = new AnamnesisQuestion();
                        newQuestion.setText(qDto.getText());
                        newQuestion.setType(qDto.getType());
                        newQuestion.setRequired(qDto.getRequired());
                        newQuestion.setCategory(qDto.getCategory());
                        newQuestion.setTemplate(template);
                        newQuestion.setCreatedAt(LocalDateTime.now());

                        if (qDto.getConditionalText() != null) {
                            newQuestion.setTriggerValue(qDto.getConditionalText().getTriggerValue());
                            newQuestion.setPlaceholder(qDto.getConditionalText().getPlaceholder());
                            newQuestion.setLabel(qDto.getConditionalText().getLabel());
                        }

                        if (qDto.getOptions() != null) {
                            qDto.getOptions().forEach(optDto -> {
                                AnamnesisQuestionOption newOption = new AnamnesisQuestionOption();
                                newOption.setValue(optDto.getValue());
                                newOption.setQuestion(newQuestion);
                                newQuestion.getOptions().add(newOption);
                            });
                        }

                        template.getQuestions().add(newQuestion);
                    }
                }
            }
            return templateRepository.save(template);
        });
    }


    public Optional<AnamnesisTemplate> toggleTemplateStatus(String id) {
        return templateRepository.findById(id).map(template -> {
            template.setIsActive(!template.getIsActive());
            template.setUpdatedAt(LocalDateTime.now());
            return templateRepository.save(template);
        });
    }

    public void deleteTemplate(String id) {
        templateRepository.deleteById(id);
    }
}
