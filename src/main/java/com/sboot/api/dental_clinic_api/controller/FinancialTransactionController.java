package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.FinancialTransactionDTO;
import com.sboot.api.dental_clinic_api.dto.FinancialTransactionResponseDTO;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import com.sboot.api.dental_clinic_api.service.FinancialTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/financial-transactions")
@RequiredArgsConstructor
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;

    @PostMapping
    public ResponseEntity<FinancialTransactionResponseDTO> create(@RequestBody FinancialTransactionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financialTransactionService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<FinancialTransactionResponseDTO>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(financialTransactionService.findAll(search, type, startDate, endDate, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransactionResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(financialTransactionService.findById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<FinancialTransactionResponseDTO>> findByType(@PathVariable TransactionType type) {
        return ResponseEntity.ok(financialTransactionService.findByType(type));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<FinancialTransactionResponseDTO>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(financialTransactionService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/type/{type}/date-range")
    public ResponseEntity<List<FinancialTransactionResponseDTO>> findByTypeAndDateRange(
            @PathVariable TransactionType type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(financialTransactionService.findByTypeAndDateRange(type, startDate, endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialTransactionResponseDTO> update(@PathVariable String id, @RequestBody FinancialTransactionDTO dto) {
        return ResponseEntity.ok(financialTransactionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        financialTransactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> calculateBalance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(financialTransactionService.calculateBalance(startDate, endDate));
    }
}