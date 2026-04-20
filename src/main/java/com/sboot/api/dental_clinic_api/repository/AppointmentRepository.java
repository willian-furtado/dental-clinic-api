package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    @Query("SELECT a FROM Appointment a " +
           "WHERE (CAST(:startDate AS date) IS NULL OR a.date >= CAST(:startDate AS date)) " +
           "AND (CAST(:endDate AS date) IS NULL OR a.date <= CAST(:endDate AS date)) " +
           "ORDER BY a.date ASC, a.time ASC")
    List<Appointment> findAllByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
