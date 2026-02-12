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
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setPatient(patient);
        
        if (appointmentDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = procedureClinicRepository.findById(appointmentDTO.getProcedureClinicId())
                    .orElseThrow(() -> new RuntimeException("Procedure Clinic not found"));
            appointment.setProcedureClinic(procedureClinic);
        }
        
        Appointment saved = appointmentRepository.save(appointment);
        
        if (appointmentDTO.getProcedureClinicId() != null) {
            ProcedureClinic procedureClinic = appointment.getProcedureClinic();
            if (procedureClinic != null && !procedureClinic.getRequiresBudget()) {
                PatientProcedure patientProcedure = new PatientProcedure();
                patientProcedure.setPatient(patient);
                patientProcedure.setProcedureClinic(procedureClinic);
                patientProcedure.setStatus(PatientProcedureStatus.planned);
                patientProcedure.setValue(procedureClinic.getBasePrice());
                patientProcedure.setScheduledDate(appointment.getDate());
                patientProcedure.setNotes(appointmentDTO.getNotes());
                patientProcedure.setOrigin(MANUAL);
                patientProcedure.setCreatedAt(java.time.LocalDateTime.now());
                
                patientProcedureRepository.save(patientProcedure);
            }
        }
        
        return appointmentMapper.toDto(saved);
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

        Appointment updated = appointmentRepository.save(existing);
        return appointmentMapper.toDto(updated);
    }

    public void delete(String id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found with id " + id);
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
