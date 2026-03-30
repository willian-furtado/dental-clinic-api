package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.MedicalCertificateDTO;
import com.sboot.api.dental_clinic_api.entity.MedicalCertificate;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.enums.CertificateType;
import com.sboot.api.dental_clinic_api.mapper.MedicalCertificateMapper;
import com.sboot.api.dental_clinic_api.repository.MedicalCertificateRepository;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalCertificateService {

    private final MedicalCertificateRepository medicalCertificateRepository;
    private final PatientRepository patientRepository;
    private final MedicalCertificateMapper medicalCertificateMapper;

    public MedicalCertificateDTO save(MedicalCertificateDTO medicalCertificateDTO) {
        log.info("Creating new medical certificate for patient ID: {}", medicalCertificateDTO.getPatientId());
        
        Patient patient = patientRepository.findById(medicalCertificateDTO.getPatientId())
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", medicalCertificateDTO.getPatientId());
                    return new RuntimeException("Patient not found with id " + medicalCertificateDTO.getPatientId());
                });

        MedicalCertificate medicalCertificate = medicalCertificateMapper.toEntity(medicalCertificateDTO);
        medicalCertificate.setPatient(patient);

        MedicalCertificate saved = medicalCertificateRepository.save(medicalCertificate);
        log.info("Successfully created medical certificate with ID: {}", saved.getId());
        return medicalCertificateMapper.toDto(saved);
    }

    public MedicalCertificateDTO update(String id, MedicalCertificateDTO medicalCertificateDTO) {
        log.info("Updating medical certificate with ID: {}", id);
        
        MedicalCertificate existing = medicalCertificateRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Medical certificate not found with ID: {}", id);
                    return new RuntimeException("Medical certificate not found with id " + id);
                });

        Patient patient = patientRepository.findById(medicalCertificateDTO.getPatientId())
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", medicalCertificateDTO.getPatientId());
                    return new RuntimeException("Patient not found with id " + medicalCertificateDTO.getPatientId());
                });

        existing.setPatient(patient);
        existing.setCertificateType(CertificateType.valueOf(medicalCertificateDTO.getCertificateType().toUpperCase()));
        existing.setStartDate(medicalCertificateDTO.getStartDate());
        existing.setEndDate(medicalCertificateDTO.getEndDate());
        existing.setStartTime(medicalCertificateDTO.getStartTime());
        existing.setEndTime(medicalCertificateDTO.getEndTime());
        existing.setReason(medicalCertificateDTO.getReason());
        existing.setObservations(medicalCertificateDTO.getObservations());
        existing.setCidCode(medicalCertificateDTO.getCidCode());
        existing.setCidDescription(medicalCertificateDTO.getCidDescription());
        existing.setDoctorName(medicalCertificateDTO.getDoctorName());
        existing.setCro(medicalCertificateDTO.getCro());

        MedicalCertificate saved = medicalCertificateRepository.save(existing);
        log.info("Successfully updated medical certificate with ID: {}", id);
        return medicalCertificateMapper.toDto(saved);
    }

    public void delete(String id) {
        log.info("Deleting medical certificate with ID: {}", id);
        
        if (!medicalCertificateRepository.existsById(id)) {
            log.error("Medical certificate not found with ID: {}", id);
            throw new RuntimeException("Medical certificate not found with id " + id);
        }
        
        medicalCertificateRepository.deleteById(id);
        log.info("Successfully deleted medical certificate with ID: {}", id);
    }

    public Page<MedicalCertificateDTO> getAll(Pageable pageable) {
        log.debug("Retrieving all medical certificates with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<MedicalCertificateDTO> result = medicalCertificateRepository.findAll(pageable)
                .map(medicalCertificateMapper::toDto);
        log.debug("Retrieved {} medical certificates", result.getTotalElements());
        return result;
    }

    public List<MedicalCertificateDTO> getAllMedicalCertificates() {
        log.debug("Retrieving all medical certificates");
        List<MedicalCertificateDTO> result = medicalCertificateRepository.findAll()
                .stream()
                .map(medicalCertificateMapper::toDto)
                .toList();
        log.debug("Retrieved {} medical certificates", result.size());
        return result;
    }

    public MedicalCertificateDTO getById(String id) {
        log.debug("Retrieving medical certificate by ID: {}", id);
        MedicalCertificate medicalCertificate = medicalCertificateRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Medical certificate not found with ID: {}", id);
                    return new RuntimeException("Medical certificate not found with id " + id);
                });
        return medicalCertificateMapper.toDto(medicalCertificate);
    }

    public List<MedicalCertificateDTO> getByPatientId(String patientId) {
        log.debug("Retrieving medical certificates for patient ID: {}", patientId);
        List<MedicalCertificate> medicalCertificates = medicalCertificateRepository.findByPatientId(patientId);
        List<MedicalCertificateDTO> result = medicalCertificates.stream()
                .map(medicalCertificateMapper::toDto)
                .toList();
        log.debug("Found {} medical certificates for patient ID: {}", result.size(), patientId);
        return result;
    }
}