package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.FinancialTransactionDTO;
import com.sboot.api.dental_clinic_api.dto.FinancialTransactionResponseDTO;
import com.sboot.api.dental_clinic_api.entity.FinancialTransaction;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.PatientProcedure;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import com.sboot.api.dental_clinic_api.mapper.FinancialTransactionMapper;
import com.sboot.api.dental_clinic_api.repository.FinancialTransactionRepository;
import com.sboot.api.dental_clinic_api.repository.PatientProcedureRepository;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final PatientRepository patientRepository;
    private final PatientProcedureRepository patientProcedureRepository;
    private final FinancialTransactionMapper mapper;

    @Transactional
    public FinancialTransactionResponseDTO create(FinancialTransactionDTO dto) {
        FinancialTransaction entity = mapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());

        if (dto.getPatientId() != null) {
            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
            entity.setPatient(patient);
        }

        if (dto.getPatientProcedureId() != null) {
            PatientProcedure procedure = patientProcedureRepository.findById(dto.getPatientProcedureId())
                    .orElseThrow(() -> new EntityNotFoundException("Patient procedure not found"));
            entity.setPatientProcedure(procedure);
        }

        entity = financialTransactionRepository.save(entity);
        return mapper.toResponseDTO(entity);
    }

    public Page<FinancialTransactionResponseDTO> findAll(String search, TransactionType type, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<FinancialTransaction> transactions = financialTransactionRepository.findAllByFilters(search, type, startDate, endDate, pageable);
        return transactions.map(mapper::toResponseDTO);
    }

    public FinancialTransactionResponseDTO findById(String id) {
        FinancialTransaction entity = financialTransactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Financial transaction not found"));
        return mapper.toResponseDTO(entity);
    }

    public List<FinancialTransactionResponseDTO> findByType(TransactionType type) {
        return financialTransactionRepository.findByType(type)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<FinancialTransactionResponseDTO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return financialTransactionRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<FinancialTransactionResponseDTO> findByTypeAndDateRange(TransactionType type, LocalDate startDate, LocalDate endDate) {
        return financialTransactionRepository.findByTypeAndDateBetween(type, startDate, endDate)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FinancialTransactionResponseDTO update(String id, FinancialTransactionDTO dto) {
        FinancialTransaction entity = financialTransactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Financial transaction not found"));

        Patient patient = null;
        if (dto.getPatientId() != null) {
            patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        }

        PatientProcedure patientProcedure = null;
        if (dto.getPatientProcedureId() != null) {
            patientProcedure = patientProcedureRepository.findById(dto.getPatientProcedureId())
                    .orElseThrow(() -> new EntityNotFoundException("Patient procedure not found"));
        }

        mapper.updateEntityFromDTO(dto, entity);
        mapper.setPatientAndProcedure(entity, patient, patientProcedure);
        entity = financialTransactionRepository.save(entity);
        return mapper.toResponseDTO(entity);
    }

    @Transactional
    public void delete(String id) {
        if (!financialTransactionRepository.existsById(id)) {
            throw new EntityNotFoundException("Financial transaction not found");
        }
        financialTransactionRepository.deleteById(id);
    }

    public BigDecimal calculateBalance(LocalDate startDate, LocalDate endDate) {
        List<FinancialTransaction> transactions = financialTransactionRepository.findByDateBetween(startDate, endDate);
        
        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return income.subtract(expense);
    }
}