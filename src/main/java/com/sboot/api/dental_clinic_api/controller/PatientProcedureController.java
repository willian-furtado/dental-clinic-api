package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.PatientProcedureDTO;
import com.sboot.api.dental_clinic_api.dto.PatientProcedureResponseDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureDTO;
import com.sboot.api.dental_clinic_api.service.PatientProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-procedures")
public class PatientProcedureController {

    @Autowired
    private PatientProcedureService patientProcedureService;

    @GetMapping("/{id}")
    public ResponseEntity<PatientProcedureDTO> findById(@PathVariable String id) {
        PatientProcedureDTO patientProcedure = patientProcedureService.findById(id);
        return ResponseEntity.ok(patientProcedure);
    }

    @GetMapping
    public ResponseEntity<Page<PatientProcedureResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String budgetId) {
        Page<PatientProcedureResponseDTO> patientProcedures = patientProcedureService.findAllWithDetails(page, size, search, patientId, budgetId);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientProcedureDTO>> findByPatientId(@PathVariable String patientId) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByPatientId(patientId);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/treatment-plan/{treatmentPlanId}")
    public ResponseEntity<List<PatientProcedureDTO>> findByTreatmentPlanId(@PathVariable String treatmentPlanId) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByTreatmentPlanId(treatmentPlanId);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/procedure-clinic/{procedureClinicId}")
    public ResponseEntity<List<PatientProcedureDTO>> findByProcedureClinicId(@PathVariable String procedureClinicId) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByProcedureClinicId(procedureClinicId);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/patient/{patientId}/status/{status}")
    public ResponseEntity<List<PatientProcedureDTO>> findByPatientIdAndStatus(@PathVariable String patientId, @PathVariable String status) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByPatientIdAndStatus(patientId, status);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/treatment-plan/{treatmentPlanId}/status/{status}")
    public ResponseEntity<List<PatientProcedureDTO>> findByTreatmentPlanIdAndStatus(@PathVariable String treatmentPlanId, @PathVariable String status) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByTreatmentPlanIdAndStatus(treatmentPlanId, status);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/procedure-clinic/{procedureClinicId}/status/{status}")
    public ResponseEntity<List<PatientProcedureDTO>> findByProcedureClinicIdAndStatus(@PathVariable String procedureClinicId, @PathVariable String status) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByProcedureClinicIdAndStatus(procedureClinicId, status);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/patient/{patientId}/scheduled-date/{scheduledDate}")
    public ResponseEntity<List<PatientProcedureDTO>> findByPatientIdAndScheduledDate(@PathVariable String patientId, @PathVariable java.time.LocalDate scheduledDate) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByPatientIdAndScheduledDate(patientId, scheduledDate);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/treatment-plan/{treatmentPlanId}/scheduled-date/{scheduledDate}")
    public ResponseEntity<List<PatientProcedureDTO>> findByTreatmentPlanIdAndScheduledDate(@PathVariable String treatmentPlanId, @PathVariable java.time.LocalDate scheduledDate) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByTreatmentPlanIdAndScheduledDate(treatmentPlanId, scheduledDate);
        return ResponseEntity.ok(patientProcedures);
    }

    @GetMapping("/procedure-clinic/{procedureClinicId}/scheduled-date/{scheduledDate}")
    public ResponseEntity<List<PatientProcedureDTO>> findByProcedureClinicIdAndScheduledDate(@PathVariable String procedureClinicId, @PathVariable java.time.LocalDate scheduledDate) {
        List<PatientProcedureDTO> patientProcedures = patientProcedureService.findByProcedureClinicIdAndScheduledDate(procedureClinicId, scheduledDate);
        return ResponseEntity.ok(patientProcedures);
    }

    @PostMapping
    public ResponseEntity<PatientProcedureDTO> save(@RequestBody PatientProcedureDTO patientProcedureDTO) {
        PatientProcedureDTO savedPatientProcedure = patientProcedureService.save(patientProcedureDTO);
        return ResponseEntity.ok(savedPatientProcedure);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientProcedureDTO> update(@PathVariable String id, @RequestBody PatientProcedureDTO patientProcedureDTO) {
        PatientProcedureDTO updatedPatientProcedure = patientProcedureService.update(id, patientProcedureDTO);
        return ResponseEntity.ok(updatedPatientProcedure);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        patientProcedureService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PatientProcedureDTO> updateStatus(
            @PathVariable String id,
            @RequestParam String status) {
        PatientProcedureDTO updatedPatientProcedure = patientProcedureService.updateStatus(id, status);
        return ResponseEntity.ok(updatedPatientProcedure);
    }

    @GetMapping("/patient/{patientId}/procedures")
    public ResponseEntity<Page<ProcedureDTO>> getProceduresByPatientId(
            @PathVariable String patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProcedureDTO> procedures = patientProcedureService.getProceduresByPatientId(patientId, page, size);
        return ResponseEntity.ok(procedures);
    }
}
