package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.DashboardResponseDTO;
import com.sboot.api.dental_clinic_api.dto.FinancialTransactionDTO;
import com.sboot.api.dental_clinic_api.dto.FinancialTransactionResponseDTO;
import com.sboot.api.dental_clinic_api.entity.FinancialTransaction;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.PatientProcedure;
import com.sboot.api.dental_clinic_api.entity.RecurringExpense;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import com.sboot.api.dental_clinic_api.mapper.FinancialTransactionMapper;
import com.sboot.api.dental_clinic_api.repository.FinancialTransactionRepository;
import com.sboot.api.dental_clinic_api.repository.PatientProcedureRepository;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import com.sboot.api.dental_clinic_api.repository.RecurringExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final PatientRepository patientRepository;
    private final PatientProcedureRepository patientProcedureRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final FinancialTransactionMapper mapper;

    @Transactional
    public FinancialTransactionResponseDTO create(FinancialTransactionDTO dto) {
        log.info("Creating financial transaction: {}", dto);
        FinancialTransaction entity = mapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());

        if (dto.getPatientId() != null) {
            log.debug("Looking up patient with id: {}", dto.getPatientId());
            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> {
                        log.warn("Patient not found with id: {}", dto.getPatientId());
                        return new EntityNotFoundException("Patient not found");
                    });
            entity.setPatient(patient);
        }

        if (dto.getPatientProcedureId() != null) {
            log.debug("Looking up patient procedure with id: {}", dto.getPatientProcedureId());
            PatientProcedure procedure = patientProcedureRepository.findById(dto.getPatientProcedureId())
                    .orElseThrow(() -> {
                        log.warn("Patient procedure not found with id: {}", dto.getPatientProcedureId());
                        return new EntityNotFoundException("Patient procedure not found");
                    });
            entity.setPatientProcedure(procedure);
        }

        if (dto.getRecurringExpenseId() != null) {
            log.debug("Looking up recurring expense with id: {}", dto.getRecurringExpenseId());
            RecurringExpense recurringExpense = recurringExpenseRepository.findById(dto.getRecurringExpenseId())
                    .orElseThrow(() -> {
                        log.warn("Recurring expense not found with id: {}", dto.getRecurringExpenseId());
                        return new EntityNotFoundException("Recurring expense not found");
                    });
            entity.setRecurringExpense(recurringExpense);
        }

        entity = financialTransactionRepository.save(entity);
        log.info("Financial transaction created successfully with id: {}", entity.getId());
        return mapper.toResponseDTO(entity);
    }

    public Page<FinancialTransactionResponseDTO> findAll(String search, TransactionType type, LocalDate startDate, LocalDate endDate, int page, int size) {
        log.info("Finding all financial transactions with filters - search: {}, type: {}, startDate: {}, endDate: {}, page: {}, size: {}",
                search, type, startDate, endDate, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<FinancialTransaction> transactions = financialTransactionRepository.findAllByFilters(search, type, startDate, endDate, pageable);
        return transactions.map(mapper::toResponseDTO);
    }

    public FinancialTransactionResponseDTO findById(String id) {
        log.info("Finding financial transaction by id: {}", id);
        FinancialTransaction entity = financialTransactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Financial transaction not found with id: {}", id);
                    return new EntityNotFoundException("Financial transaction not found");
                });
        log.info("Financial transaction found with id: {}", id);
        return mapper.toResponseDTO(entity);
    }

    public List<FinancialTransactionResponseDTO> findByType(TransactionType type) {
        log.info("Finding financial transactions by type: {}", type);
        List<FinancialTransactionResponseDTO> result = financialTransactionRepository.findByType(type)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Found {} financial transactions of type: {}", result.size(), type);
        return result;
    }

    public List<FinancialTransactionResponseDTO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Finding financial transactions by date range: {} to {}", startDate, endDate);
        List<FinancialTransactionResponseDTO> result = financialTransactionRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Found {} financial transactions in date range: {} to {}", result.size(), startDate, endDate);
        return result;
    }

    public List<FinancialTransactionResponseDTO> findByTypeAndDateRange(TransactionType type, LocalDate startDate, LocalDate endDate) {
        log.info("Finding financial transactions by type: {} and date range: {} to {}", type, startDate, endDate);
        List<FinancialTransactionResponseDTO> result = financialTransactionRepository.findByTypeAndDateBetween(type, startDate, endDate)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Found {} financial transactions of type: {} in date range: {} to {}", result.size(), type, startDate, endDate);
        return result;
    }

    @Transactional
    public FinancialTransactionResponseDTO update(String id, FinancialTransactionDTO dto) {
        log.info("Updating financial transaction with id: {} - {}", id, dto);
        FinancialTransaction entity = financialTransactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Financial transaction not found with id: {}", id);
                    return new EntityNotFoundException("Financial transaction not found");
                });

        Patient patient = null;
        if (dto.getPatientId() != null) {
            log.debug("Looking up patient with id: {}", dto.getPatientId());
            patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> {
                        log.warn("Patient not found with id: {}", dto.getPatientId());
                        return new EntityNotFoundException("Patient not found");
                    });
        }

        PatientProcedure patientProcedure = null;
        if (dto.getPatientProcedureId() != null) {
            log.debug("Looking up patient procedure with id: {}", dto.getPatientProcedureId());
            patientProcedure = patientProcedureRepository.findById(dto.getPatientProcedureId())
                    .orElseThrow(() -> {
                        log.warn("Patient procedure not found with id: {}", dto.getPatientProcedureId());
                        return new EntityNotFoundException("Patient procedure not found");
                    });
        }

        mapper.updateEntityFromDTO(dto, entity);
        mapper.setPatientAndProcedure(entity, patient, patientProcedure);
        entity = financialTransactionRepository.save(entity);
        log.info("Financial transaction updated successfully with id: {}", entity.getId());
        return mapper.toResponseDTO(entity);
    }

    @Transactional
    public void delete(String id) {
        log.info("Deleting financial transaction with id: {}", id);
        if (!financialTransactionRepository.existsById(id)) {
            log.warn("Financial transaction not found with id: {}", id);
            throw new EntityNotFoundException("Financial transaction not found");
        }
        financialTransactionRepository.deleteById(id);
        log.info("Financial transaction deleted successfully with id: {}", id);
    }

    public BigDecimal calculateBalance(LocalDate startDate, LocalDate endDate) {
        log.info("Calculating balance for date range: {} to {}", startDate, endDate);
        List<FinancialTransaction> transactions = financialTransactionRepository.findByDateBetween(startDate, endDate);
        log.debug("Found {} transactions for balance calculation", transactions.size());

        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = income.subtract(expense);
        log.info("Balance calculated - income: {}, expense: {}, balance: {}", income, expense, balance);
        return balance;
    }

    public DashboardResponseDTO getDashboard(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching dashboard for date range: {} to {}", startDate, endDate);
        DashboardResponseDTO dashboard = financialTransactionRepository.findDashboardByDateRange(startDate, endDate);
        log.info("Dashboard fetched - revenues: {}, expenses: {}, recurring: {}, non-recurring: {}, netProfit: {}",
                dashboard.getTotalRevenues(), dashboard.getTotalExpenses(),
                dashboard.getRecurringExpenses(), dashboard.getNonRecurringExpenses(),
                dashboard.getNetProfit());
        return dashboard;
    }
}
