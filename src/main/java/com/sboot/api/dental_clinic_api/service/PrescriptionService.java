package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.PrescriptionDTO;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.Prescription;
import com.sboot.api.dental_clinic_api.entity.PrescriptionMedication;
import com.sboot.api.dental_clinic_api.mapper.PrescriptionMapper;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import com.sboot.api.dental_clinic_api.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Transactional
    public PrescriptionDTO save(PrescriptionDTO prescriptionDTO) {
        Patient patient = patientRepository.findById(prescriptionDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription.setPatient(patient);

        // Set prescription reference in medications
        if (prescription.getMedications() != null) {
            prescription.getMedications().forEach(med -> med.setPrescription(prescription));
        }

        Prescription saved = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(saved);
    }

    @Transactional
    public PrescriptionDTO update(String id, PrescriptionDTO prescriptionDTO) {
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id " + id));

        Patient patient = patientRepository.findById(prescriptionDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.setPatient(patient);
        existing.setObservations(prescriptionDTO.getObservations());
        existing.setDoctorName(prescriptionDTO.getDoctorName());
        existing.setCro(prescriptionDTO.getCro());

        // Update medications
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
        return prescriptionMapper.toDto(updated);
    }

    public void delete(String id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new RuntimeException("Prescription not found with id " + id);
        }
        prescriptionRepository.deleteById(id);
    }

    public List<PrescriptionDTO> getAllByPatientId(String patientId) {
        return prescriptionRepository.findByPatientId(patientId)
                .stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    public PrescriptionDTO getById(String id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id " + id));
        return prescriptionMapper.toDto(prescription);
    }
}
