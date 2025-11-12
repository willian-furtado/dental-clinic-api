package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.PrescriptionDTO;
import com.sboot.api.dental_clinic_api.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @GetMapping("/{id}")
    public PrescriptionDTO getById(@PathVariable String id) {
        return prescriptionService.getById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<PrescriptionDTO> getAllByPatientId(@PathVariable String patientId) {
        return prescriptionService.getAllByPatientId(patientId);
    }

    @PostMapping
    public PrescriptionDTO create(@RequestBody PrescriptionDTO prescriptionDTO) {
        return prescriptionService.save(prescriptionDTO);
    }

    @PutMapping("/{id}")
    public PrescriptionDTO update(@PathVariable String id, @RequestBody PrescriptionDTO prescriptionDTO) {
        return prescriptionService.update(id, prescriptionDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        prescriptionService.delete(id);
    }
}
