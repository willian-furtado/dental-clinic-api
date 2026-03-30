package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.entity.AnamnesisQuestion;
import com.sboot.api.dental_clinic_api.repository.AnamnesisQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnamnesisQuestionService {

    private final AnamnesisQuestionRepository questionRepository;

    @Transactional
    public void deleteQuestion(String id) {
        log.info("Attempting to delete anamnesis question with ID: {}", id);
        
        AnamnesisQuestion question = questionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Anamnesis question not found with ID: {}", id);
                    return new EntityNotFoundException("Question not found with id: " + id);
                });
        
        questionRepository.delete(question);
        log.info("Successfully deleted anamnesis question with ID: {}", id);
    }
}