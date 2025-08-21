package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.service.AnamnesisQuestionOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class AnamnesisQuestionOptionController {

    private final AnamnesisQuestionOptionService optionService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable String id) {
        optionService.deleteOption(id);
        return ResponseEntity.noContent().build();
    }
}
