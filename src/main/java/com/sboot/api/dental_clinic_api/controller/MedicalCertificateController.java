package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.MedicalCertificateDTO;
import com.sboot.api.dental_clinic_api.service.MedicalCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-certificates")
@RequiredArgsConstructor
public class MedicalCertificateController {

    private final MedicalCertificateService medicalCertificateService;

    @GetMapping
    public Page<MedicalCertificateDTO> getAll(Pageable pageable) {
        return medicalCertificateService.getAll(pageable);
    }

    @GetMapping("/list")
    public List<MedicalCertificateDTO> getAllMedicalCertificates() {
        return medicalCertificateService.getAllMedicalCertificates();
    }

    @GetMapping("/{id}")
    public MedicalCertificateDTO getById(@PathVariable String id) {
        return medicalCertificateService.getById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalCertificateDTO> getByPatientId(@PathVariable String patientId) {
        return medicalCertificateService.getByPatientId(patientId);
    }

    @PostMapping
    public MedicalCertificateDTO create(@RequestBody MedicalCertificateDTO medicalCertificateDTO) {
        return medicalCertificateService.save(medicalCertificateDTO);
    }

    @PutMapping("/{id}")
    public MedicalCertificateDTO update(@PathVariable String id, @RequestBody MedicalCertificateDTO medicalCertificateDTO) {
        return medicalCertificateService.update(id, medicalCertificateDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        medicalCertificateService.delete(id);
    }
}
