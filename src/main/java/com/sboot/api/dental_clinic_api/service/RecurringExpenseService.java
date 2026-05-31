package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.RecurringExpenseRequestDTO;
import com.sboot.api.dental_clinic_api.dto.RecurringExpenseResponseDTO;
import com.sboot.api.dental_clinic_api.entity.RecurringExpense;
import com.sboot.api.dental_clinic_api.mapper.RecurringExpenseMapper;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecurringExpenseService {

    private final RecurringExpenseRepository repository;
    private final RecurringExpenseMapper mapper;

    @Transactional
    public RecurringExpenseResponseDTO create(RecurringExpenseRequestDTO dto) {
        log.info("Creating recurring expense: {}", dto);
        RecurringExpense entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        log.info("Recurring expense created successfully with id: {}", entity.getId());
        return mapper.toResponseDTO(entity);
    }

    public Page<RecurringExpenseResponseDTO> findAll(int page, int size, String search) {
        log.info("Finding all recurring expenses with search: {}, page: {}, size: {}", search, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<RecurringExpense> expenses = repository.findAllByFilters(search, pageable);
        return expenses.map(mapper::toResponseDTO);
    }

    public RecurringExpenseResponseDTO findById(String id) {
        log.info("Finding recurring expense by id: {}", id);
        RecurringExpense entity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Recurring expense not found with id: {}", id);
                    return new EntityNotFoundException("Recurring expense not found");
                });
        log.info("Recurring expense found with id: {}", id);
        return mapper.toResponseDTO(entity);
    }

    @Transactional
    public RecurringExpenseResponseDTO update(String id, RecurringExpenseRequestDTO dto) {
        log.info("Updating recurring expense with id: {} - {}", id, dto);
        RecurringExpense entity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Recurring expense not found with id: {}", id);
                    return new EntityNotFoundException("Recurring expense not found");
                });

        mapper.updateEntityFromDTO(dto, entity);
        entity = repository.save(entity);
        log.info("Recurring expense updated successfully with id: {}", entity.getId());
        return mapper.toResponseDTO(entity);
    }

    @Transactional
    public void delete(String id) {
        log.info("Deleting recurring expense with id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Recurring expense not found with id: {}", id);
            throw new EntityNotFoundException("Recurring expense not found");
        }
        repository.deleteById(id);
        log.info("Recurring expense deleted successfully with id: {}", id);
    }

    public List<RecurringExpenseResponseDTO> findActive() {
        log.info("Finding all active recurring expenses");
        List<RecurringExpenseResponseDTO> result = repository.findByIsActiveTrue()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
        log.info("Found {} active recurring expenses", result.size());
        return result;
    }

    public List<RecurringExpenseResponseDTO> findByCategory(String category) {
        log.info("Finding recurring expenses by category: {}", category);
        List<RecurringExpenseResponseDTO> result = repository.findByCategory(category)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
        log.info("Found {} recurring expenses of category: {}", result.size(), category);
        return result;
    }

    public List<RecurringExpenseResponseDTO> findByDueDay(Integer dueDay) {
        log.info("Finding recurring expenses by due day: {}", dueDay);
        List<RecurringExpenseResponseDTO> result = repository.findByDueDay(dueDay)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
        log.info("Found {} recurring expenses with due day: {}", result.size(), dueDay);
        return result;
    }
}