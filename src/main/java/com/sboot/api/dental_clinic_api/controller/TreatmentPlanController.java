package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.TreatmentPlanRequestDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanResponseDTO;
import com.sboot.api.dental_clinic_api.enums.TreatmentPlanStatus;
import com.sboot.api.dental_clinic_api.service.TreatmentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatment-plans")
@RequiredArgsConstructor
public class TreatmentPlanController {

    private final TreatmentPlanService service;

    @GetMapping
    public ResponseEntity<Page<TreatmentPlanResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.findPageAll(page, size, search, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentPlanResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TreatmentPlanResponseDTO>> getByPatientId(@PathVariable String patientId) {
        return ResponseEntity.ok(service.findByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<TreatmentPlanResponseDTO> create(@RequestBody TreatmentPlanRequestDTO request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentPlanResponseDTO> update(@PathVariable String id, @RequestBody TreatmentPlanRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TreatmentPlanResponseDTO> updateStatus(@PathVariable String id, @RequestParam TreatmentPlanStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

}
