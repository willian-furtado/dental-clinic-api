package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.TreatmentPlanRequestDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanResponseDTO;
import com.sboot.api.dental_clinic_api.entity.*;
import com.sboot.api.dental_clinic_api.enums.PatientProcedureStatus;
import com.sboot.api.dental_clinic_api.enums.TreatmentPlanStatus;
import com.sboot.api.dental_clinic_api.mapper.TreatmentPlanContractMapper;
import com.sboot.api.dental_clinic_api.mapper.TreatmentPlanMapper;
import com.sboot.api.dental_clinic_api.mapper.TreatmentPlanProcedureMapper;
import com.sboot.api.dental_clinic_api.mapper.TreatmentPlanTermsMapper;
import com.sboot.api.dental_clinic_api.repository.*;
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
public class TreatmentPlanService {

    public static final String AUTOMATICO = "Automático";
    private final TreatmentPlanRepository repository;
    private final TreatmentPlanProcedureRepository procedureRepository;
    private final TreatmentPlanTermsRepository termsRepository;
    private final TreatmentPlanContractRepository contractRepository;
    private final PatientProcedureRepository patientProcedureRepository;
    private final TreatmentPlanMapper mapper;
    private final TreatmentPlanProcedureMapper procedureMapper;
    private final TreatmentPlanTermsMapper termsMapper;
    private final TreatmentPlanContractMapper contractMapper;

    public Page<TreatmentPlanResponseDTO> findPageAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<TreatmentPlan> result = repository.findAllByFilters(search, pageable);
        return result.map(mapper::toResponseDTO);
    }

    public TreatmentPlanResponseDTO findById(String id) {
        TreatmentPlan treatmentPlan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TreatmentPlan not found with id: " + id));
        return mapper.toResponseDTO(treatmentPlan);
    }

    public List<TreatmentPlanResponseDTO> findByPatientId(String patientId) {
        List<TreatmentPlan> result = repository.findByPatientId(patientId);
        return result.stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public TreatmentPlanResponseDTO create(TreatmentPlanRequestDTO request) {
        TreatmentPlan entity = mapper.toEntity(request);

        Long nextCode = repository.getNextSequenceValue();
        entity.setCode(nextCode);

        if (entity.getPatient() == null && request.getPatientId() != null) {
            Patient patient = new Patient();
            patient.setId(request.getPatientId());
            entity.setPatient(patient);
        }

        if (request.getProcedures() != null && !request.getProcedures().isEmpty()) {
            List<TreatmentPlanProcedure> procedures = request.getProcedures().stream()
                    .map(procedureMapper::toEntity)
                    .collect(Collectors.toList());
            procedures.forEach(p -> p.setTreatmentPlan(entity));
            entity.setProcedures(procedures);
        }

        if (request.getTerms() != null) {
            TreatmentPlanTerms terms = termsMapper.toEntity(request.getTerms());
            terms.setTreatmentPlan(entity);
            entity.setTerms(terms);
        }

        if (request.getContract() != null) {
            TreatmentPlanContract contract = contractMapper.toEntity(request.getContract());
            contract.setTreatmentPlan(entity);
            entity.setContract(contract);
        }
        entity.setCreatedAt(LocalDateTime.now());
        entity.setValidUntil(LocalDate.now().plusMonths(3));

        TreatmentPlan saved = repository.save(entity);
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    public TreatmentPlanResponseDTO update(String id, TreatmentPlanRequestDTO request) {
        TreatmentPlan existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TreatmentPlan not found with id: " + id));

        if (existing.getPatient() == null) {
            Patient patient = new Patient();
            patient.setId(request.getPatientId());
            existing.setPatient(patient);
        } else {
            existing.getPatient().setId(request.getPatientId());
        }

        existing.setSubtotal(request.getSubtotal());
        existing.setTotal(request.getTotal());
        existing.setStatus(request.getStatus());
        existing.setNotes(request.getNotes());
        existing.setValidUntil(request.getValidUntil());
        existing.setFinalValue(request.getFinalValue());
        existing.setPaymentDiscount(request.getPaymentDiscount());
        existing.setPaymentDiscountType(request.getPaymentDiscountType());
        existing.setPaymentDiscountAmount(request.getPaymentDiscountAmount());

        if (request.getProcedures() != null) {
            procedureRepository.deleteByTreatmentPlanId(id);

            List<TreatmentPlanProcedure> procedures = request.getProcedures().stream()
                    .map(procedureMapper::toEntity)
                    .collect(Collectors.toList());
            procedures.forEach(p -> p.setTreatmentPlan(existing));
            existing.setProcedures(procedures);
        }

        if (request.getTerms() != null) {
            TreatmentPlanTerms terms = termsMapper.toEntity(request.getTerms());
            terms.setTreatmentPlan(existing);
            termsRepository.save(terms);
            existing.setTerms(terms);
        }

        if (request.getContract() != null) {
            TreatmentPlanContract contract = contractMapper.toEntity(request.getContract());
            contract.setTreatmentPlan(existing);
            contractRepository.save(contract);
            existing.setContract(contract);
        }

        TreatmentPlan saved = repository.save(existing);
        
        if (saved.getStatus() == TreatmentPlanStatus.approved && saved.getProcedures() != null && !saved.getProcedures().isEmpty()) {
            updatePatientProceduresFromTreatmentPlan(saved);
        }
        
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    private void updatePatientProceduresFromTreatmentPlan(TreatmentPlan treatmentPlan) {
        patientProcedureRepository.deleteByTreatmentPlanId(treatmentPlan.getId());
        
        BigDecimal totalDiscount = treatmentPlan.getPaymentDiscountAmount() != null ? treatmentPlan.getPaymentDiscountAmount() : BigDecimal.ZERO;
        BigDecimal totalValue = treatmentPlan.getSubtotal() != null ? treatmentPlan.getSubtotal() : BigDecimal.ZERO;
        String discountType = String.valueOf(treatmentPlan.getPaymentDiscountType());
        
        BigDecimal proceduresTotalValue = treatmentPlan.getProcedures().stream()
                .map(proc -> proc.getValue() != null ? proc.getValue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal discountRatio = BigDecimal.ONE;
        if (totalValue.compareTo(BigDecimal.ZERO) > 0 && proceduresTotalValue.compareTo(BigDecimal.ZERO) > 0) {
            if ("percentage".equalsIgnoreCase(discountType)) {
                BigDecimal percentage = totalDiscount.divide(new BigDecimal("100"), 10, java.math.RoundingMode.HALF_UP);
                discountRatio = BigDecimal.ONE.subtract(percentage);
            } else {
                discountRatio = treatmentPlan.getFinalValue().divide(totalValue, 10, java.math.RoundingMode.HALF_UP);
            }
        }
        
        BigDecimal totalDiscountedValue = BigDecimal.ZERO;
        List<PatientProcedure> patientProcedures = new java.util.ArrayList<>();
        
        for (TreatmentPlanProcedure procedure : treatmentPlan.getProcedures()) {
            PatientProcedure patientProcedure = new PatientProcedure();
            patientProcedure.setPatient(treatmentPlan.getPatient());
            patientProcedure.setTreatmentPlan(treatmentPlan);
            patientProcedure.setProcedureClinic(procedure.getProcedureClinic());
            patientProcedure.setToothNumber(procedure.getToothNumber());
            patientProcedure.setFaces(procedure.getFaces());
            
            BigDecimal procedureValue = procedure.getValue() != null ? procedure.getValue() : BigDecimal.ZERO;
            
            BigDecimal finalProcedureValue = procedureValue.multiply(discountRatio).setScale(2, java.math.RoundingMode.HALF_UP);
            
            patientProcedure.setValue(finalProcedureValue);
            patientProcedure.setStatus(PatientProcedureStatus.planned);
            patientProcedure.setScheduledDate(null);
            patientProcedure.setNotes(procedure.getNotes());
            patientProcedure.setOrigin(AUTOMATICO);
            patientProcedure.setCreatedAt(java.time.LocalDateTime.now());
            
            patientProcedures.add(patientProcedure);
            totalDiscountedValue = totalDiscountedValue.add(finalProcedureValue);
        }
        
        if (!patientProcedures.isEmpty()) {
            BigDecimal targetTotal = treatmentPlan.getFinalValue() != null ? treatmentPlan.getFinalValue() : BigDecimal.ZERO;
            BigDecimal difference = targetTotal.subtract(totalDiscountedValue);
            
            if (difference.abs().compareTo(new BigDecimal("0.01")) > 0) {
                PatientProcedure lastProcedure = patientProcedures.get(patientProcedures.size() - 1);
                BigDecimal adjustedValue = lastProcedure.getValue().add(difference);
                lastProcedure.setValue(adjustedValue.setScale(2, java.math.RoundingMode.HALF_UP));
            }
            
            for (PatientProcedure patientProcedure : patientProcedures) {
                patientProcedureRepository.save(patientProcedure);
            }
        }
    }

    @Transactional
    public void delete(String id) {
        TreatmentPlan treatmentPlan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TreatmentPlan not found with id: " + id));

        if (treatmentPlan.getProcedures() != null) {
            for (TreatmentPlanProcedure procedure : treatmentPlan.getProcedures()) {
                patientProcedureRepository.deleteByTreatmentPlanIdAndProcedureClinicId(id, procedure.getProcedureClinic().getId());
            }
        }

        repository.deleteById(id);
    }

    @Transactional
    public TreatmentPlanResponseDTO updateStatus(String id, TreatmentPlanStatus status) {
        TreatmentPlan treatmentPlan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TreatmentPlan not found with id: " + id));

        treatmentPlan.setStatus(status);
        TreatmentPlan saved = repository.save(treatmentPlan);

        if (status == TreatmentPlanStatus.approved && saved.getProcedures() != null && !saved.getProcedures().isEmpty()) {
            updatePatientProceduresFromTreatmentPlan(saved);
        }

        return mapper.toResponseDTO(saved);
    }
}
