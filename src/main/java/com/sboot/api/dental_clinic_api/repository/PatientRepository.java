package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    @Query(value = "SELECT * FROM patients WHERE EXTRACT(MONTH FROM birth_date::date) = EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(DAY FROM birth_date::date) = EXTRACT(DAY FROM CURRENT_DATE)", nativeQuery = true)
    List<Patient> findPatientsWithTodayBirthday();

    @Query("SELECT p FROM Patient p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR p.cpf LIKE CONCAT('%', :search, '%')")
    Page<Patient> findByNameOrCpf(Pageable pageable, String search);
}
