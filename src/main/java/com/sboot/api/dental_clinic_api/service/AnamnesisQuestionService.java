package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.entity.AnamnesisQuestion;
import com.sboot.api.dental_clinic_api.repository.AnamnesisQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnamnesisQuestionService {

    private final AnamnesisQuestionRepository questionRepository;

    @Transactional
    public void deleteQuestion(String id) {
        AnamnesisQuestion question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + id));
        questionRepository.delete(question);
    }
}
