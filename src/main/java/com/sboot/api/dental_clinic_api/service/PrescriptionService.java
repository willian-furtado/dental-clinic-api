package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.PrescriptionDTO;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.Prescription;
import com.sboot.api.dental_clinic_api.entity.PrescriptionMedication;
import com.sboot.api.dental_clinic_api.mapper.PrescriptionMapper;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import com.sboot.api.dental_clinic_api.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Transactional
    public PrescriptionDTO save(PrescriptionDTO prescriptionDTO) {
        log.info("Creating new prescription for patient ID: {}", prescriptionDTO.getPatientId());
        
        Patient patient = patientRepository.findById(prescriptionDTO.getPatientId())
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", prescriptionDTO.getPatientId());
                    return new RuntimeException("Patient not found");
                });

        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription.setPatient(patient);

        if (prescription.getMedications() != null) {
            prescription.getMedications().forEach(med -> med.setPrescription(prescription));
        }

        Prescription saved = prescriptionRepository.save(prescription);
        log.info("Successfully created prescription with ID: {}", saved.getId());
        return prescriptionMapper.toDto(saved);
    }

    @Transactional
    public PrescriptionDTO update(String id, PrescriptionDTO prescriptionDTO) {
        log.info("Updating prescription with ID: {}", id);
        
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Prescription not found with ID: {}", id);
                    return new RuntimeException("Prescription not found with id " + id);
                });

        Patient patient = patientRepository.findById(prescriptionDTO.getPatientId())
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", prescriptionDTO.getPatientId());
                    return new RuntimeException("Patient not found");
                });

        existing.setPatient(patient);
        existing.setObservations(prescriptionDTO.getObservations());
        existing.setDoctorName(prescriptionDTO.getDoctorName());
        existing.setCro(prescriptionDTO.getCro());

        existing.getMedications().clear();
        if (prescriptionDTO.getMedications() != null) {
            List<PrescriptionMedication> medications = prescriptionDTO.getMedications().stream()
                    .map(dto -> PrescriptionMedication.builder()
                            .prescription(existing)
                            .name(dto.getName())
                            .dosage(dto.getDosage())
                            .frequency(dto.getFrequency())
                            .duration(dto.getDuration())
                            .instructions(dto.getInstructions())
                            .build())
                    .toList();
            existing.getMedications().addAll(medications);
        }

        Prescription updated = prescriptionRepository.save(existing);
        log.info("Successfully updated prescription with ID: {}", id);
        return prescriptionMapper.toDto(updated);
    }

    public void delete(String id) {
        log.info("Deleting prescription with ID: {}", id);
        
        if (!prescriptionRepository.existsById(id)) {
            log.error("Prescription not found with ID: {}", id);
            throw new RuntimeException("Prescription not found with id " + id);
        }
        
        prescriptionRepository.deleteById(id);
        log.info("Successfully deleted prescription with ID: {}", id);
    }

    public List<PrescriptionDTO> getAllByPatientId(String patientId) {
        log.debug("Retrieving prescriptions for patient ID: {}", patientId);
        List<PrescriptionDTO> result = prescriptionRepository.findByPatientId(patientId)
                .stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Found {} prescriptions for patient ID: {}", result.size(), patientId);
        return result;
    }

    public PrescriptionDTO getById(String id) {
        log.debug("Retrieving prescription by ID: {}", id);
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Prescription not found with ID: {}", id);
                    return new RuntimeException("Prescription not found with id " + id);
                });
        return prescriptionMapper.toDto(prescription);
    }
}