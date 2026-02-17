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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    public static final String MANUAL = "Manual";
    private final AppointmentRepository appointmentRepository;
    private final PatientProcedureRepository patientProcedureRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;
    private final ProcedureClinicRepository procedureClinicRepository;

    @Transactional
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        Patient patient = findPatientById(appointmentDTO.getPatientId());
        Appointment appointment = createAppointment(appointmentDTO, patient);
        Appointment saved = appointmentRepository.save(appointment);

        handlePatientProcedure(appointmentDTO, appointment, saved, patient);

        return appointmentMapper.toDto(saved);
    }

    private Patient findPatientById(String patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    private Appointment createAppointment(AppointmentDTO appointmentDTO, Patient patient) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setPatient(patient);

        if (appointmentDTO.getPatientProcedureId() != null) {
            PatientProcedure patientProcedure = patientProcedureRepository.findById(appointmentDTO.getPatientProcedureId())
                    .orElseThrow(() -> new RuntimeException("Patient Procedure not found"));
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
                .orElseThrow(() -> new RuntimeException("Procedure Clinic not found"));
    }

    private void handlePatientProcedure(AppointmentDTO appointmentDTO, Appointment appointment, Appointment saved, Patient patient) {
        if (appointmentDTO.getPatientProcedureId() != null) {
            updateExistingPatientProcedure(appointmentDTO.getPatientProcedureId(), appointment);
        } else if (appointmentDTO.getProcedureClinicId() != null) {
            createNewPatientProcedure(appointment, saved, patient, appointmentDTO.getNotes());
        }
    }

    private void updateExistingPatientProcedure(String patientProcedureId, Appointment appointment) {
        PatientProcedure patientProcedure = patientProcedureRepository.findById(patientProcedureId)
                .orElseThrow(() -> new RuntimeException("Patient Procedure not found"));
        patientProcedure.setScheduledDate(appointment.getDate());
        patientProcedure.setAppointment(appointment);
        patientProcedureRepository.save(patientProcedure);
    }

    private void createNewPatientProcedure(Appointment appointment, Appointment saved, Patient patient, String notes) {
        ProcedureClinic procedureClinic = appointment.getProcedureClinic();
        if (procedureClinic != null && !procedureClinic.getRequiresBudget()) {
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
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));

        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.setDate(appointmentDTO.getDate() != null ? java.time.LocalDate.parse(appointmentDTO.getDate()) : existing.getDate());
        existing.setTime(appointmentDTO.getTime() != null ? java.time.LocalTime.parse(appointmentDTO.getTime()) : existing.getTime());
        existing.setPatient(patient);

        if (appointmentDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = procedureClinicRepository.findById(appointmentDTO.getProcedureClinicId())
                    .orElseThrow(() -> new RuntimeException("Procedure Clinic not found"));
            existing.setProcedureClinic(procedureClinic);
        } else {
            existing.setProcedureClinic(null);
        }

        existing.setDuration(appointmentDTO.getDuration());
        existing.setStatus(AppointmentStatus.valueOf(appointmentDTO.getStatus().toUpperCase()));
        existing.setNotes(appointmentDTO.getNotes());

        if (Objects.equals(appointmentDTO.getStatus(), "cancelled") && existing.getPatientProcedure() != null) {
            PatientProcedure patientProcedure = existing.getPatientProcedure();
            patientProcedure.setStatus(PatientProcedureStatus.cancelled);
            patientProcedureRepository.save(patientProcedure);
        }

        Appointment updated = appointmentRepository.save(existing);
        if (appointmentDTO.getPatientProcedureId() != null) {
            updateExistingPatientProcedure(appointmentDTO.getPatientProcedureId(), updated);
        }
        return appointmentMapper.toDto(updated);
    }

    public void delete(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));

        if (appointment.getPatientProcedure() != null) {
            patientProcedureRepository.delete(appointment.getPatientProcedure());
        }

        appointmentRepository.deleteById(id);
    }

    public Page<AppointmentDTO> getAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable)
                .map(appointmentMapper::toDto);
    }

    public AppointmentDTO getById(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));
        return appointmentMapper.toDto(appointment);
    }

    public AppointmentDTO confirmAppointment(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment updated = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updated);
    }
}
