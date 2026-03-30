package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.PatientProcedureDTO;
import com.sboot.api.dental_clinic_api.dto.PatientProcedureResponseDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientProcedureService {

    private final PatientProcedureRepository patientProcedureRepository;

    private final PatientProcedureMapper patientProcedureMapper;

    private final PatientRepository patientRepository;

    private final TreatmentPlanRepository treatmentPlanRepository;

    private final ProcedureClinicRepository procedureClinicRepository;

    public PatientProcedureDTO findById(String id) {
        log.debug("Retrieving patient procedure by ID: {}", id);
        PatientProcedure patientProcedure = patientProcedureRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("PatientProcedure not found with ID: {}", id);
                    return new RuntimeException("PatientProcedure not found with id: " + id);
                });
        return patientProcedureMapper.toDTO(patientProcedure);
    }

    public Page<PatientProcedureResponseDTO> findAllWithDetails(int page, int size, String search, String patientId, String budgetId) {
        log.debug("Retrieving patient procedures with filters - page: {}, size: {}, search: {}, patientId: {}, budgetId: {}", 
                page, size, search, patientId, budgetId);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<PatientProcedure> result = patientProcedureRepository.findAllByFilters(search, patientId, budgetId, pageable);
        log.debug("Found {} patient procedures", result.getTotalElements());
        return result.map(patientProcedureMapper::toResponseDTO);
    }

    public List<PatientProcedureDTO> findByPatientId(String patientId) {
        log.debug("Retrieving patient procedures for patient ID: {}", patientId);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByPatientId(patientId);
        log.debug("Found {} procedures for patient ID: {}", patientProcedures.size(), patientId);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByTreatmentPlanId(String treatmentPlanId) {
        log.debug("Retrieving patient procedures for treatment plan ID: {}", treatmentPlanId);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByTreatmentPlanId(treatmentPlanId);
        log.debug("Found {} procedures for treatment plan ID: {}", patientProcedures.size(), treatmentPlanId);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByProcedureClinicId(String procedureClinicId) {
        log.debug("Retrieving patient procedures for procedure clinic ID: {}", procedureClinicId);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByProcedureClinicId(procedureClinicId);
        log.debug("Found {} procedures for procedure clinic ID: {}", patientProcedures.size(), procedureClinicId);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByPatientIdAndStatus(String patientId, String status) {
        log.debug("Retrieving patient procedures for patient ID: {} with status: {}", patientId, status);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByPatientIdAndStatus(patientId, status);
        log.debug("Found {} procedures for patient ID: {} with status: {}", patientProcedures.size(), patientId, status);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByTreatmentPlanIdAndStatus(String treatmentPlanId, String status) {
        log.debug("Retrieving patient procedures for treatment plan ID: {} with status: {}", treatmentPlanId, status);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByTreatmentPlanIdAndStatus(treatmentPlanId, status);
        log.debug("Found {} procedures for treatment plan ID: {} with status: {}", patientProcedures.size(), treatmentPlanId, status);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByProcedureClinicIdAndStatus(String procedureClinicId, String status) {
        log.debug("Retrieving patient procedures for procedure clinic ID: {} with status: {}", procedureClinicId, status);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByProcedureClinicIdAndStatus(procedureClinicId, status);
        log.debug("Found {} procedures for procedure clinic ID: {} with status: {}", patientProcedures.size(), procedureClinicId, status);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByPatientIdAndScheduledDate(String patientId, java.time.LocalDate scheduledDate) {
        log.debug("Retrieving patient procedures for patient ID: {} with scheduled date: {}", patientId, scheduledDate);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByPatientIdAndScheduledDate(patientId, scheduledDate);
        log.debug("Found {} procedures for patient ID: {} with scheduled date: {}", patientProcedures.size(), patientId, scheduledDate);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByTreatmentPlanIdAndScheduledDate(String treatmentPlanId, java.time.LocalDate scheduledDate) {
        log.debug("Retrieving patient procedures for treatment plan ID: {} with scheduled date: {}", treatmentPlanId, scheduledDate);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByTreatmentPlanIdAndScheduledDate(treatmentPlanId, scheduledDate);
        log.debug("Found {} procedures for treatment plan ID: {} with scheduled date: {}", patientProcedures.size(), treatmentPlanId, scheduledDate);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PatientProcedureDTO> findByProcedureClinicIdAndScheduledDate(String procedureClinicId, java.time.LocalDate scheduledDate) {
        log.debug("Retrieving patient procedures for procedure clinic ID: {} with scheduled date: {}", procedureClinicId, scheduledDate);
        List<PatientProcedure> patientProcedures = patientProcedureRepository.findByProcedureClinicIdAndScheduledDate(procedureClinicId, scheduledDate);
        log.debug("Found {} procedures for procedure clinic ID: {} with scheduled date: {}", patientProcedures.size(), procedureClinicId, scheduledDate);
        return patientProcedures.stream()
                .map(patientProcedureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PatientProcedureDTO save(PatientProcedureDTO patientProcedureDTO) {
        log.info("Creating new patient procedure for patient ID: {}", patientProcedureDTO.getPatientId());
        PatientProcedure patientProcedure = patientProcedureMapper.toEntity(patientProcedureDTO);

        if (patientProcedureDTO.getTreatmentPlanId() != null) {
            patientProcedure.setOrigin("Automático");
        } else if (patientProcedureDTO.getProcedureClinicId() != null) {
            patientProcedure.setOrigin("Manual");
        }

        if (patientProcedureDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(patientProcedureDTO.getPatientId())
                    .orElseThrow(() -> {
                        log.error("Patient not found with ID: {}", patientProcedureDTO.getPatientId());
                        return new RuntimeException("Patient not found with id: " + patientProcedureDTO.getPatientId());
                    });
            patientProcedure.setPatient(patient);
        }

        if (patientProcedureDTO.getTreatmentPlanId() != null) {
            TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(patientProcedureDTO.getTreatmentPlanId())
                    .orElseThrow(() -> {
                        log.error("TreatmentPlan not found with ID: {}", patientProcedureDTO.getTreatmentPlanId());
                        return new RuntimeException("TreatmentPlan not found with id: " + patientProcedureDTO.getTreatmentPlanId());
                    });
            patientProcedure.setTreatmentPlan(treatmentPlan);
        }

        if (patientProcedureDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = procedureClinicRepository.findById(patientProcedureDTO.getProcedureClinicId())
                    .orElseThrow(() -> {
                        log.error("ProcedureClinic not found with ID: {}", patientProcedureDTO.getProcedureClinicId());
                        return new RuntimeException("ProcedureClinic not found with id: " + patientProcedureDTO.getProcedureClinicId());
                    });
            patientProcedure.setProcedureClinic(procedureClinic);
        }

        PatientProcedure savedPatientProcedure = patientProcedureRepository.save(patientProcedure);
        log.info("Successfully created patient procedure with ID: {}", savedPatientProcedure.getId());
        return patientProcedureMapper.toDTO(savedPatientProcedure);
    }

    public PatientProcedureDTO update(String id, PatientProcedureDTO patientProcedureDTO) {
        log.info("Updating patient procedure with ID: {}", id);
        PatientProcedure existingPatientProcedure = patientProcedureRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("PatientProcedure not found with ID: {}", id);
                    return new RuntimeException("PatientProcedure not found with id: " + id);
                });

        existingPatientProcedure.setToothNumber(patientProcedureDTO.getToothNumber());
        existingPatientProcedure.setFaces(patientProcedureDTO.getFaces());
        existingPatientProcedure.setValue(patientProcedureDTO.getValue());
        existingPatientProcedure.setStatus(patientProcedureDTO.getStatus());
        existingPatientProcedure.setScheduledDate(patientProcedureDTO.getScheduledDate());
        existingPatientProcedure.setNotes(patientProcedureDTO.getNotes());

        if (patientProcedureDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(patientProcedureDTO.getPatientId())
                    .orElseThrow(() -> {
                        log.error("Patient not found with ID: {}", patientProcedureDTO.getPatientId());
                        return new RuntimeException("Patient not found with id: " + patientProcedureDTO.getPatientId());
                    });
            existingPatientProcedure.setPatient(patient);
        }

        if (patientProcedureDTO.getTreatmentPlanId() != null) {
            TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(patientProcedureDTO.getTreatmentPlanId())
                    .orElseThrow(() -> {
                        log.error("TreatmentPlan not found with ID: {}", patientProcedureDTO.getTreatmentPlanId());
                        return new RuntimeException("TreatmentPlan not found with id: " + patientProcedureDTO.getTreatmentPlanId());
                    });
            existingPatientProcedure.setTreatmentPlan(treatmentPlan);
        }

        if (patientProcedureDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = procedureClinicRepository.findById(patientProcedureDTO.getProcedureClinicId())
                    .orElseThrow(() -> {
                        log.error("ProcedureClinic not found with ID: {}", patientProcedureDTO.getProcedureClinicId());
                        return new RuntimeException("ProcedureClinic not found with id: " + patientProcedureDTO.getProcedureClinicId());
                    });
            existingPatientProcedure.setProcedureClinic(procedureClinic);
        }

        PatientProcedure updatedPatientProcedure = patientProcedureRepository.save(existingPatientProcedure);
        log.info("Successfully updated patient procedure with ID: {}", id);
        return patientProcedureMapper.toDTO(updatedPatientProcedure);
    }

    public void deleteById(String id) {
        log.info("Deleting patient procedure with ID: {}", id);
        PatientProcedure patientProcedure = patientProcedureRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("PatientProcedure not found with ID: {}", id);
                    return new RuntimeException("PatientProcedure not found with id: " + id);
                });
        patientProcedureRepository.delete(patientProcedure);
        log.info("Successfully deleted patient procedure with ID: {}", id);
    }

    public PatientProcedureDTO updateStatus(String id, String status) {
        log.info("Updating status for patient procedure ID: {} to status: {}", id, status);
        PatientProcedure patientProcedure = patientProcedureRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("PatientProcedure not found with ID: {}", id);
                    return new RuntimeException("PatientProcedure not found with id: " + id);
                });

        try {
            PatientProcedureStatus statusEnum = PatientProcedureStatus.valueOf(status);
            patientProcedure.setStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status value: {}", status);
            throw new RuntimeException("Invalid status: " + status);
        }

        PatientProcedure updatedPatientProcedure = patientProcedureRepository.save(patientProcedure);
        log.info("Successfully updated status for patient procedure ID: {} to status: {}", id, status);
        return patientProcedureMapper.toDTO(updatedPatientProcedure);
    }

    public Page<ProcedureDTO> getProceduresByPatientId(String patientId, int page, int size) {
        log.debug("Retrieving procedures for patient ID: {} with pagination: page={}, size={}", patientId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "scheduledDate");
        Page<PatientProcedure> patientProcedures = patientProcedureRepository.findByPatientId(patientId, pageable);
        log.debug("Found {} procedures for patient ID: {}", patientProcedures.getTotalElements(), patientId);
        return patientProcedures.map(patientProcedureMapper::toProcedureDTO);
    }
}