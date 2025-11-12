package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.repository.AnamnesisQuestionOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnamnesisQuestionOptionService {

    private final AnamnesisQuestionOptionRepository optionRepository;

    public void deleteOption(String optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new IllegalArgumentException("Option not found: " + optionId);
        }
        optionRepository.deleteById(optionId);
    }
}