package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.MedicalCertificateDTO;
import com.sboot.api.dental_clinic_api.entity.MedicalCertificate;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.enums.CertificateType;
import com.sboot.api.dental_clinic_api.mapper.MedicalCertificateMapper;
import com.sboot.api.dental_clinic_api.repository.MedicalCertificateRepository;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalCertificateService {

    private final MedicalCertificateRepository medicalCertificateRepository;
    private final PatientRepository patientRepository;
    private final MedicalCertificateMapper medicalCertificateMapper;

    public MedicalCertificateDTO save(MedicalCertificateDTO medicalCertificateDTO) {
        Patient patient = patientRepository.findById(medicalCertificateDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + medicalCertificateDTO.getPatientId()));

        MedicalCertificate medicalCertificate = medicalCertificateMapper.toEntity(medicalCertificateDTO);
        medicalCertificate.setPatient(patient);

        MedicalCertificate saved = medicalCertificateRepository.save(medicalCertificate);
        return medicalCertificateMapper.toDto(saved);
    }

    public MedicalCertificateDTO update(String id, MedicalCertificateDTO medicalCertificateDTO) {
        MedicalCertificate existing = medicalCertificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical certificate not found with id " + id));

        Patient patient = patientRepository.findById(medicalCertificateDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + medicalCertificateDTO.getPatientId()));

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
        return medicalCertificateMapper.toDto(saved);
    }

    public void delete(String id) {
        if (!medicalCertificateRepository.existsById(id)) {
            throw new RuntimeException("Medical certificate not found with id " + id);
        }
        medicalCertificateRepository.deleteById(id);
    }

    public Page<MedicalCertificateDTO> getAll(Pageable pageable) {
        return medicalCertificateRepository.findAll(pageable)
                .map(medicalCertificateMapper::toDto);
    }

    public List<MedicalCertificateDTO> getAllMedicalCertificates() {
        return medicalCertificateRepository.findAll()
                .stream()
                .map(medicalCertificateMapper::toDto)
                .toList();
    }

    public MedicalCertificateDTO getById(String id) {
        MedicalCertificate medicalCertificate = medicalCertificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical certificate not found with id " + id));
        return medicalCertificateMapper.toDto(medicalCertificate);
    }

    public List<MedicalCertificateDTO> getByPatientId(String patientId) {
        List<MedicalCertificate> medicalCertificates = medicalCertificateRepository.findByPatientId(patientId);
        return medicalCertificates.stream()
                .map(medicalCertificateMapper::toDto)
                .toList();
    }
}
