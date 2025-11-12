package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.AnamnesisTemplateDTO;
import com.sboot.api.dental_clinic_api.service.AnamnesisTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class AnamnesisTemplateController {

    private final AnamnesisTemplateService templateService;

    @GetMapping
    public ResponseEntity<List<AnamnesisTemplateDTO>> getAllTemplates(
            @RequestParam(required = false) String audience,
            @RequestParam(required = false) String search
    ) {
        List<AnamnesisTemplateDTO> templates = templateService.getAllTemplates(audience, search);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnamnesisTemplateDTO> getTemplateById(@PathVariable String id) {
        return templateService.getTemplateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnamnesisTemplateDTO> createTemplate(@RequestBody AnamnesisTemplateDTO template) {
        AnamnesisTemplateDTO created = templateService.createTemplate(template);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnamnesisTemplateDTO> updateTemplate(
            @PathVariable String id,
            @RequestBody AnamnesisTemplateDTO updatedTemplate
    ) {
        return templateService.updateTemplate(id, updatedTemplate)
                .map(template -> ResponseEntity.ok(templateService.getTemplateById(template.getId()).orElse(null)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<AnamnesisTemplateDTO> toggleTemplateStatus(@PathVariable String id) {
        return templateService.toggleTemplateStatus(id)
                .map(template -> ResponseEntity.ok(templateService.getTemplateById(template.getId()).orElse(null)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
