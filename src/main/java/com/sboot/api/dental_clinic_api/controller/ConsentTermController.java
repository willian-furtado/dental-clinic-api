package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.ConsentTermGenerationRequestDTO;
import com.sboot.api.dental_clinic_api.dto.ConsentTermResponseDTO;
import com.sboot.api.dental_clinic_api.service.ConsentTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consent-term")
@RequiredArgsConstructor
public class ConsentTermController {
    private final ConsentTermService consentTermService;

    @PostMapping("/generate")
    public ResponseEntity<ConsentTermResponseDTO> generateConsentTerm(@RequestBody ConsentTermGenerationRequestDTO request) {
        return ResponseEntity.ok(consentTermService.generateConsentTerm(request));
    }
}

