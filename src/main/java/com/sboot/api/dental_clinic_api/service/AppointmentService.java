package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.AppointmentDTO;
import com.sboot.api.dental_clinic_api.entity.Appointment;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.PatientProcedure;
import com.sboot.api.dental_clinic_api.entity.ProcedureClinic;
import com.sboot.api.dental_clinic_api.enums.AppointmentStatus;
import com.sboot.api.dental_clinic_api.enums.PatientProcedureStatus;
import com.sboot.api.dental_clinic_api.mapper.AppointmentMapper;
import com.sboot.api.dental_clinic_api.repository.AppointmentRepository;
import com.sboot.api.dental_clinic_api.repository.PatientProcedureRepository;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import com.sboot.api.dental_clinic_api.repository.ProcedureClinicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    public static final String MANUAL = "Manual";
    private final AppointmentRepository appointmentRepository;
    private final PatientProcedureRepository patientProcedureRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;
    private final ProcedureClinicRepository procedureClinicRepository;

    @Transactional
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        log.info("Creating new appointment for patient ID: {}", appointmentDTO.getPatientId());
        
        Patient patient = findPatientById(appointmentDTO.getPatientId());
        Appointment appointment = createAppointment(appointmentDTO, patient);
        Appointment saved = appointmentRepository.save(appointment);

        handlePatientProcedure(appointmentDTO, appointment, saved, patient);

        log.info("Successfully created appointment with ID: {}", saved.getId());
        return appointmentMapper.toDto(saved);
    }

    private Patient findPatientById(String patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", patientId);
                    return new RuntimeException("Patient not found");
                });
    }

    private Appointment createAppointment(AppointmentDTO appointmentDTO, Patient patient) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setPatient(patient);

        if (appointmentDTO.getPatientProcedureId() != null) {
            PatientProcedure patientProcedure = patientProcedureRepository.findById(appointmentDTO.getPatientProcedureId())
                    .orElseThrow(() -> {
                        log.error("Patient Procedure not found with ID: {}", appointmentDTO.getPatientProcedureId());
                        return new RuntimeException("Patient Procedure not found");
                    });
            appointment.setPatientProcedure(patientProcedure);
        }

        if (appointmentDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = findProcedureClinicById(appointmentDTO.getProcedureClinicId());
            appointment.setProcedureClinic(procedureClinic);
        }

        return appointment;
    }

    private ProcedureClinic findProcedureClinicById(String procedureClinicId) {
        return procedureClinicRepository.findById(procedureClinicId)
                .orElseThrow(() -> {
                    log.error("Procedure Clinic not found with ID: {}", procedureClinicId);
                    return new RuntimeException("Procedure Clinic not found");
                });
    }

    private void handlePatientProcedure(AppointmentDTO appointmentDTO, Appointment appointment, Appointment saved, Patient patient) {
        if (appointmentDTO.getPatientProcedureId() != null) {
            updateExistingPatientProcedure(appointmentDTO.getPatientProcedureId(), appointment);
        } else if (appointmentDTO.getProcedureClinicId() != null) {
            createNewPatientProcedure(appointment, saved, patient, appointmentDTO.getNotes());
        }
    }

    private void updateExistingPatientProcedure(String patientProcedureId, Appointment appointment) {
        log.debug("Updating existing patient procedure with ID: {}", patientProcedureId);
        
        PatientProcedure patientProcedure = patientProcedureRepository.findById(patientProcedureId)
                .orElseThrow(() -> {
                    log.error("Patient Procedure not found with ID: {}", patientProcedureId);
                    return new RuntimeException("Patient Procedure not found");
                });
        patientProcedure.setScheduledDate(appointment.getDate());
        patientProcedure.setAppointment(appointment);
        patientProcedureRepository.save(patientProcedure);
    }

    private void createNewPatientProcedure(Appointment appointment, Appointment saved, Patient patient, String notes) {
        ProcedureClinic procedureClinic = appointment.getProcedureClinic();
        if (procedureClinic != null && !procedureClinic.getRequiresBudget()) {
            log.debug("Creating new patient procedure for appointment");
            
            PatientProcedure patientProcedure = buildPatientProcedure(patient, procedureClinic, appointment.getDate(), notes, saved);
            PatientProcedure savedPatientProcedure = patientProcedureRepository.save(patientProcedure);
            saved.setPatientProcedure(savedPatientProcedure);
            appointmentRepository.save(saved);
        }
    }

    private PatientProcedure buildPatientProcedure(Patient patient, ProcedureClinic procedureClinic,
                                                   java.time.LocalDate scheduledDate, String notes, Appointment appointment) {
        PatientProcedure patientProcedure = new PatientProcedure();
        patientProcedure.setPatient(patient);
        patientProcedure.setProcedureClinic(procedureClinic);
        patientProcedure.setStatus(PatientProcedureStatus.planned);
        patientProcedure.setValue(procedureClinic.getBasePrice());
        patientProcedure.setScheduledDate(scheduledDate);
        patientProcedure.setNotes(notes);
        patientProcedure.setOrigin(MANUAL);
        patientProcedure.setAppointment(appointment);
        patientProcedure.setCreatedAt(java.time.LocalDateTime.now());
        return patientProcedure;
    }

    public AppointmentDTO update(String id, AppointmentDTO appointmentDTO) {
        log.info("Updating appointment with ID: {}", id);
        
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Appointment not found with ID: {}", id);
                    return new RuntimeException("Appointment not found with id " + id);
                });

        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", appointmentDTO.getPatientId());
                    return new RuntimeException("Patient not found");
                });

        existing.setDate(appointmentDTO.getDate() != null ? java.time.LocalDate.parse(appointmentDTO.getDate()) : existing.getDate());
        existing.setTime(appointmentDTO.getTime() != null ? java.time.LocalTime.parse(appointmentDTO.getTime()) : existing.getTime());
        existing.setPatient(patient);

        if (appointmentDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = procedureClinicRepository.findById(appointmentDTO.getProcedureClinicId())
                    .orElseThrow(() -> {
                        log.error("Procedure Clinic not found with ID: {}", appointmentDTO.getProcedureClinicId());
                        return new RuntimeException("Procedure Clinic not found");
                    });
            existing.setProcedureClinic(procedureClinic);
        } else {
            existing.setProcedureClinic(null);
        }

        existing.setDuration(appointmentDTO.getDuration());
        existing.setStatus(AppointmentStatus.valueOf(appointmentDTO.getStatus().toUpperCase()));
        existing.setNotes(appointmentDTO.getNotes());

        if (Objects.equals(appointmentDTO.getStatus(), "cancelled") && existing.getPatientProcedure() != null) {
            log.info("Cancelling patient procedure for appointment ID: {}", id);
            PatientProcedure patientProcedure = existing.getPatientProcedure();
            patientProcedure.setStatus(PatientProcedureStatus.cancelled);
            patientProcedureRepository.save(patientProcedure);
        }

        Appointment updated = appointmentRepository.save(existing);
        if (appointmentDTO.getPatientProcedureId() != null) {
            updateExistingPatientProcedure(appointmentDTO.getPatientProcedureId(), updated);
        }
        
        log.info("Successfully updated appointment with ID: {}", id);
        return appointmentMapper.toDto(updated);
    }

    public void delete(String id) {
        log.info("Deleting appointment with ID: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Appointment not found with ID: {}", id);
                    return new RuntimeException("Appointment not found with id " + id);
                });

        if (appointment.getPatientProcedure() != null) {
            log.debug("Deleting associated patient procedure for appointment ID: {}", id);
            patientProcedureRepository.delete(appointment.getPatientProcedure());
        }

        appointmentRepository.deleteById(id);
        log.info("Successfully deleted appointment with ID: {}", id);
    }

    public List<AppointmentDTO> getAll(String startDate, String endDate) {
        log.debug("Retrieving all appointments with filters: startDate={}, endDate={}", startDate, endDate);
        
        LocalDate start = startDate != null && !startDate.isBlank() ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null && !endDate.isBlank() ? LocalDate.parse(endDate) : null;
        
        List<Appointment> appointments = appointmentRepository.findAllByDateRange(start, end);
        
        List<AppointmentDTO> result = appointments.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
                
        log.debug("Retrieved {} appointments", result.size());
        return result;
    }

    public AppointmentDTO getById(String id) {
        log.debug("Retrieving appointment by ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Appointment not found with ID: {}", id);
                    return new RuntimeException("Appointment not found with id " + id);
                });
        return appointmentMapper.toDto(appointment);
    }

    public AppointmentDTO confirmAppointment(String id) {
        log.info("Confirming appointment with ID: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Appointment not found with ID: {}", id);
                    return new RuntimeException("Appointment not found with id " + id);
                });

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment updated = appointmentRepository.save(appointment);
        
        log.info("Successfully confirmed appointment with ID: {}", id);
        return appointmentMapper.toDto(updated);
    }
}