package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.ProcedureClinicRequestDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureClinicResponseDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureClinicStatsDTO;
import com.sboot.api.dental_clinic_api.service.ProcedureClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/procedures-clinic")
@RequiredArgsConstructor
public class ProcedureClinicController {

    private final ProcedureClinicService service;

    @GetMapping
    public ResponseEntity<Page<ProcedureClinicResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(service.findPageAll(page, size, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedureClinicResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProcedureClinicResponseDTO> create(@RequestBody ProcedureClinicRequestDTO request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedureClinicResponseDTO> update(@PathVariable String id, @RequestBody ProcedureClinicRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ProcedureClinicResponseDTO> toggleStatus(@PathVariable String id) {
        return ResponseEntity.ok(service.toggleStatus(id));
    }

    @GetMapping("/stats")
    public ResponseEntity<ProcedureClinicStatsDTO> getStatistics() {
        return ResponseEntity.ok(service.getStatistics());
    }
}
