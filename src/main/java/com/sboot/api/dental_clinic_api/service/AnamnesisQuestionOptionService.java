package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.repository.AnamnesisQuestionOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnamnesisQuestionOptionService {

    private final AnamnesisQuestionOptionRepository optionRepository;

    public void deleteOption(String optionId) {
        log.info("Attempting to delete anamnesis question option with ID: {}", optionId);
        
        if (!optionRepository.existsById(optionId)) {
            log.warn("Anamnesis question option not found with ID: {}", optionId);
            throw new IllegalArgumentException("Option not found: " + optionId);
        }
        
        optionRepository.deleteById(optionId);
        log.info("Successfully deleted anamnesis question option with ID: {}", optionId);
    }
}