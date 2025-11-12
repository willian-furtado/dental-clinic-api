package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.PatientDTO;
import com.sboot.api.dental_clinic_api.dto.PatientDocumentDTO;
import com.sboot.api.dental_clinic_api.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public Page<PatientDTO> getAll(Pageable pageable,
                                   @RequestParam(required = false, defaultValue = "") String search) {
        return patientService.getAll(pageable, search);
    }

    @GetMapping("/list")
    public List<PatientDTO> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public PatientDTO getById(@PathVariable String id) {
        return patientService.getById(id);
    }

    @PostMapping
    public PatientDTO create(@RequestBody PatientDTO patientDTO) {
        return patientService.save(patientDTO);
    }

    @PutMapping("/{id}")
    public PatientDTO update(@PathVariable String id, @RequestBody PatientDTO patientDTO) {
        return patientService.update(id, patientDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        patientService.delete(id);
    }

    @DeleteMapping("/{id}/photo")
    public void removePhoto(@PathVariable String id) {
        patientService.removePhoto(id);
    }

    @DeleteMapping("/{patientId}/documents/{documentId}")
    public void deleteDocument(@PathVariable String patientId, @PathVariable String documentId) {
        patientService.deleteDocument(patientId, documentId);
    }

    @PostMapping("/{patientId}/documents")
    public PatientDocumentDTO saveDocument(@PathVariable String patientId, @RequestBody PatientDocumentDTO documentDTO) {
        return patientService.saveDocument(patientId, documentDTO);
    }

    @GetMapping("/birthdays")
    public List<PatientDTO> getTodayBirthdays() {
        return patientService.getTodayBirthdays();
    }

}
