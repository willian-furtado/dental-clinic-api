package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
}
