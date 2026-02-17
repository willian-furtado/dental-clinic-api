package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.PatientProcedureDTO;
import com.sboot.api.dental_clinic_api.dto.PatientProcedureResponseDTO;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.PatientProcedure;
import com.sboot.api.dental_clinic_api.entity.ProcedureClinic;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlan;
import com.sboot.api.dental_clinic_api.enums.PatientProcedureStatus;
import com.sboot.api.dental_clinic_api.mapper.PatientProcedureMapper;
import com.sboot.api.dental_clinic_api.repository.PatientProcedureRepository;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import com.sboot.api.dental_clinic_api.repository.ProcedureClinicRepository;
import com.sboot.api.dental_clinic_api.repository.TreatmentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientProcedureService {

    private final PatientProcedureRepository patientProcedureRepository;

    private final PatientProcedureMapper patientProcedureMapper;

    private final PatientRepository patientRepository;

    private final TreatmentPlanRepository treatmentPlanRepository;

    private final ProcedureClinicRepository procedureClinicRepository;

    public PatientProcedureDTO findById(String id) {
        PatientProcedure patientProcedure = patientProcedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientProcedure not found with id: " + id));
        return patientProcedureMapper.toDTO(patientProcedure);
    }

    public Page<PatientProcedureResponseDTO> findAllWithDetails(int page, int size, String search, String patientId, String budgetId) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<PatientProcedure> result = patientProcedureRepository.findAllByFilters(search, patientId, budgetId, pageable);
        return result.map(patientProcedureMapper::toResponseDTO);
    }

    public List<PatientProcedureDTO> findByPatientId(String patientId) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByPatientId(patientId);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByTreatmentPlanId(String treatmentPlanId) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByTreatmentPlanId(treatmentPlanId);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByProcedureClinicId(String procedureClinicId) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByProcedureClinicId(procedureClinicId);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByPatientIdAndStatus(String patientId, String status) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByPatientIdAndStatus(patientId, status);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByTreatmentPlanIdAndStatus(String treatmentPlanId, String status) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByTreatmentPlanIdAndStatus(treatmentPlanId, status);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByProcedureClinicIdAndStatus(String procedureClinicId, String status) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByProcedureClinicIdAndStatus(procedureClinicId, status);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByPatientIdAndScheduledDate(String patientId, java.time.LocalDate scheduledDate) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByPatientIdAndScheduledDate(patientId, scheduledDate);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByTreatmentPlanIdAndScheduledDate(String treatmentPlanId, java.time.LocalDate scheduledDate) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByTreatmentPlanIdAndScheduledDate(treatmentPlanId, scheduledDate);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByProcedureClinicIdAndScheduledDate(String procedureClinicId, java.time.LocalDate scheduledDate) {
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByProcedureClinicIdAndScheduledDate(procedureClinicId, scheduledDate);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PatientProcedureDTO save(PatientProcedureDTO patientProcedureDTO) {
        PatientProcedure patientProcedure = patientProcedureMapper.toEntity(patientProcedureDTO);

        if (patientProcedureDTO.getTreatmentPlanId() != null) {
            patientProcedure.setOrigin("Automático");
        } else if (patientProcedureDTO.getProcedureClinicId() != null) {
            patientProcedure.setOrigin("Manual");
        }

        if (patientProcedureDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(patientProcedureDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientProcedureDTO.getPatientId()));
            patientProcedure.setPatient(patient);
        }

        if (patientProcedureDTO.getTreatmentPlanId() != null) {
            TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(patientProcedureDTO.getTreatmentPlanId())
                    .orElseThrow(() -> new RuntimeException("TreatmentPlan not found with id: " + patientProcedureDTO.getTreatmentPlanId()));
            patientProcedure.setTreatmentPlan(treatmentPlan);
        }

        if (patientProcedureDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = procedureClinicRepository.findById(patientProcedureDTO.getProcedureClinicId())
                    .orElseThrow(() -> new RuntimeException("ProcedureClinic not found with id: " + patientProcedureDTO.getProcedureClinicId()));
            patientProcedure.setProcedureClinic(procedureClinic);
        }

        PatientProcedure savedPatientProcedure = patientProcedureRepository.save(patientProcedure);
        return patientProcedureMapper.toDTO(savedPatientProcedure);
    }

    public PatientProcedureDTO update(String id, PatientProcedureDTO patientProcedureDTO) {
        PatientProcedure existingPatientProcedure = patientProcedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientProcedure not found with id: " + id));

        existingPatientProcedure.setToothNumber(patientProcedureDTO.getToothNumber());
        existingPatientProcedure.setFaces(patientProcedureDTO.getFaces());
        existingPatientProcedure.setValue(patientProcedureDTO.getValue());
        existingPatientProcedure.setStatus(patientProcedureDTO.getStatus());
        existingPatientProcedure.setScheduledDate(patientProcedureDTO.getScheduledDate());
        existingPatientProcedure.setNotes(patientProcedureDTO.getNotes());

        if (patientProcedureDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(patientProcedureDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientProcedureDTO.getPatientId()));
            existingPatientProcedure.setPatient(patient);
        }

        if (patientProcedureDTO.getTreatmentPlanId() != null) {
            TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(patientProcedureDTO.getTreatmentPlanId())
                    .orElseThrow(() -> new RuntimeException("TreatmentPlan not found with id: " + patientProcedureDTO.getTreatmentPlanId()));
            existingPatientProcedure.setTreatmentPlan(treatmentPlan);
        }

        if (patientProcedureDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = procedureClinicRepository.findById(patientProcedureDTO.getProcedureClinicId())
                    .orElseThrow(() -> new RuntimeException("ProcedureClinic not found with id: " + patientProcedureDTO.getProcedureClinicId()));
            existingPatientProcedure.setProcedureClinic(procedureClinic);
        }

        PatientProcedure updatedPatientProcedure = patientProcedureRepository.save(existingPatientProcedure);
        return patientProcedureMapper.toDTO(updatedPatientProcedure);
    }

    public void deleteById(String id) {
        PatientProcedure patientProcedure = patientProcedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientProcedure not found with id: " + id));
        patientProcedureRepository.delete(patientProcedure);
    }

    public PatientProcedureDTO updateStatus(String id, String status) {
        PatientProcedure patientProcedure = patientProcedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientProcedure not found with id: " + id));

        try {
            PatientProcedureStatus statusEnum = PatientProcedureStatus.valueOf(status);
            patientProcedure.setStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }

        PatientProcedure updatedPatientProcedure = patientProcedureRepository.save(patientProcedure);
        return patientProcedureMapper.toDTO(updatedPatientProcedure);
    }
}
