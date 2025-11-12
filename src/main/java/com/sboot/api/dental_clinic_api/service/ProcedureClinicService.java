package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.ProcedureClinicRequestDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureClinicResponseDTO;
import com.sboot.api.dental_clinic_api.dto.ProcedureClinicStatsDTO;
import com.sboot.api.dental_clinic_api.entity.ProcedureClinic;
import com.sboot.api.dental_clinic_api.mapper.ProcedureClinicMapper;
import com.sboot.api.dental_clinic_api.repository.ProcedureClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ProcedureClinicService {
    private final ProcedureClinicRepository repository;
    private final ProcedureClinicMapper mapper;

    public Page<ProcedureClinicResponseDTO> findPageAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");
        Page<ProcedureClinic> result;

        if (search != null && !search.trim().isEmpty()) {
            result = repository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(search, search, pageable);
        } else {
            result = repository.findAll(pageable);
        }

        return result.map(mapper::toResponse);
    }

    public ProcedureClinicResponseDTO findById(String id) {
        ProcedureClinic procedure = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedure not found"));
        return mapper.toResponse(procedure);
    }

    public ProcedureClinicResponseDTO create(ProcedureClinicRequestDTO request) {
        ProcedureClinic procedure = mapper.toEntity(request);
        return mapper.toResponse(repository.save(procedure));
    }

    public ProcedureClinicResponseDTO update(String id, ProcedureClinicRequestDTO request) {
        ProcedureClinic procedure = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedure not found"));

        procedure.setName(request.getName());
        procedure.setCategory(request.getCategory());
        procedure.setDescription(request.getDescription());
        procedure.setBasePrice(request.getBasePrice());
        procedure.setDuration(request.getDuration());
        procedure.setIsActive(request.getIsActive());

        return mapper.toResponse(repository.save(procedure));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public ProcedureClinicResponseDTO toggleStatus(String id) {
        ProcedureClinic procedure = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedure not found"));

        procedure.setIsActive(!procedure.getIsActive());

        return mapper.toResponse(repository.save(procedure));
    }

    public ProcedureClinicStatsDTO getStatistics() {
        Long total = repository.countAllProcedures();
        Long active = repository.countActiveProcedures();
        var avg = repository.getAveragePrice();
        if (avg == null) {
            avg = BigDecimal.ZERO;
        }

        avg = avg.setScale(2, RoundingMode.HALF_UP);
        return new ProcedureClinicStatsDTO(total, active, avg);
    }
}
