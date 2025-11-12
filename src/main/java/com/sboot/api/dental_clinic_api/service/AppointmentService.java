package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.AppointmentDTO;
import com.sboot.api.dental_clinic_api.entity.Appointment;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.mapper.AppointmentMapper;
import com.sboot.api.dental_clinic_api.repository.AppointmentRepository;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;

    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setPatient(patient);
        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(saved);
    }

    public AppointmentDTO update(String id, AppointmentDTO appointmentDTO) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));

        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Appointment updatedEntity = appointmentMapper.toEntity(appointmentDTO);
        existing.setDate(updatedEntity.getDate());
        existing.setTime(updatedEntity.getTime());
        existing.setPatient(patient);
        existing.setType(appointmentDTO.getType());
        existing.setDuration(appointmentDTO.getDuration());
        existing.setStatus(updatedEntity.getStatus());
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
}
