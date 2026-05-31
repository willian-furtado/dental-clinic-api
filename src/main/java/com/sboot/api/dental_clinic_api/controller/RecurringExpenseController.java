package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.RecurringExpenseRequestDTO;
import com.sboot.api.dental_clinic_api.dto.RecurringExpenseResponseDTO;
import com.sboot.api.dental_clinic_api.service.RecurringExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring-expenses")
@RequiredArgsConstructor
public class RecurringExpenseController {

    private final RecurringExpenseService service;

    @GetMapping
    public ResponseEntity<Page<RecurringExpenseResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(service.findAll(page, size, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecurringExpenseResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<RecurringExpenseResponseDTO> create(@RequestBody RecurringExpenseRequestDTO request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringExpenseResponseDTO> update(@PathVariable String id, @RequestBody RecurringExpenseRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<RecurringExpenseResponseDTO>> getActive() {
        return ResponseEntity.ok(service.findActive());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<RecurringExpenseResponseDTO>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.findByCategory(category));
    }

    @GetMapping("/due-day/{dueDay}")
    public ResponseEntity<List<RecurringExpenseResponseDTO>> getByDueDay(@PathVariable Integer dueDay) {
        return ResponseEntity.ok(service.findByDueDay(dueDay));
    }
}