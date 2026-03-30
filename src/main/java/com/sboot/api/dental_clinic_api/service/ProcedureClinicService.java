package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.ProcedureClinicRequestDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureClinicResponseDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureClinicStatsDTO;
import com.sboot.api.dental_clinic_api.entity.ProcedureClinic;
import com.sboot.api.dental_clinic_api.mapper.ProcedureClinicMapper;
import com.sboot.api.dental_clinic_api.repository.ProcedureClinicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcedureClinicService {
    public static final String GERAL = "Geral";
    private final ProcedureClinicRepository repository;
    private final ProcedureClinicMapper mapper;

    public Page<ProcedureClinicResponseDTO> findPageAll(int page, int size, String search) {
        log.debug("Retrieving procedures with pagination: page={}, size={}, search={}", page, size, search);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");
        Page<ProcedureClinic> result = repository.findAllProcedures(pageable, search);
        log.debug("Found {} procedures", result.getTotalElements());
        return result.map(mapper::toResponse);
    }

    public List<ProcedureClinicResponseDTO> findByCategoryNotGeneral() {
        log.debug("Retrieving procedures by category not general");
        List<ProcedureClinic> result = repository.findByCategoryNot(GERAL);
        log.debug("Found {} procedures with category not general", result.size());
        return result.stream().map(mapper::toResponse).toList();
    }

    public List<ProcedureClinicResponseDTO> findByRequiresBudgetTrue() {
        log.debug("Retrieving procedures that require budget");
        List<ProcedureClinic> result = repository.findByRequiresBudgetTrue();
        log.debug("Found {} procedures that require budget", result.size());
        return result.stream().map(mapper::toResponse).toList();
    }

    public ProcedureClinicResponseDTO findById(String id) {
        log.debug("Retrieving procedure by ID: {}", id);
        ProcedureClinic procedure = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Procedure not found with ID: {}", id);
                    return new RuntimeException("Procedure not found");
                });
        return mapper.toResponse(procedure);
    }

    public ProcedureClinicResponseDTO create(ProcedureClinicRequestDTO request) {
        log.info("Creating new procedure with name: {}", request.getName());
        ProcedureClinic procedure = mapper.toEntity(request);
        ProcedureClinicResponseDTO saved = mapper.toResponse(repository.save(procedure));
        log.info("Successfully created procedure with ID: {}", saved.getId());
        return saved;
    }

    public ProcedureClinicResponseDTO update(String id, ProcedureClinicRequestDTO request) {
        log.info("Updating procedure with ID: {}", id);
        ProcedureClinic procedure = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Procedure not found with ID: {}", id);
                    return new RuntimeException("Procedure not found");
                });

        procedure.setName(request.getName());
        procedure.setCategory(request.getCategory());
        procedure.setDescription(request.getDescription());
        procedure.setBasePrice(request.getBasePrice());
        procedure.setDuration(request.getDuration());
        procedure.setIsActive(request.getIsActive());
        procedure.setRequiresBudget(request.getRequiresBudget());

        ProcedureClinicResponseDTO updated = mapper.toResponse(repository.save(procedure));
        log.info("Successfully updated procedure with ID: {}", id);
        return updated;
    }

    public void delete(String id) {
        log.info("Deleting procedure with ID: {}", id);
        repository.deleteById(id);
        log.info("Successfully deleted procedure with ID: {}", id);
    }

    public ProcedureClinicResponseDTO toggleStatus(String id) {
        log.info("Toggling status for procedure with ID: {}", id);
        ProcedureClinic procedure = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Procedure not found with ID: {}", id);
                    return new RuntimeException("Procedure not found");
                });

        procedure.setIsActive(!procedure.getIsActive());
        ProcedureClinicResponseDTO updated = mapper.toResponse(repository.save(procedure));
        log.info("Successfully toggled status for procedure ID: {} to active: {}", id, procedure.getIsActive());
        return updated;
    }

    public ProcedureClinicStatsDTO getStatistics() {
        log.debug("Retrieving procedure statistics");
        Long total = repository.countAllProcedures();
        Long active = repository.countActiveProcedures();
        var avg = repository.getAveragePrice();
        if (avg == null) {
            avg = BigDecimal.ZERO;
        }

        avg = avg.setScale(2, RoundingMode.HALF_UP);
        log.debug("Procedure statistics - total: {}, active: {}, average price: {}", total, active, avg);
        return new ProcedureClinicStatsDTO(total, active, avg);
    }
}