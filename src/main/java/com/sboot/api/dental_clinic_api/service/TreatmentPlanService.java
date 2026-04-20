package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.BudgetDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanContractDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanRequestDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanResponseDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanTermsDTO;
import com.sboot.api.dental_clinic_api.entity.*;
import com.sboot.api.dental_clinic_api.enums.PatientProcedureStatus;
import com.sboot.api.dental_clinic_api.enums.TreatmentPlanStatus;
import com.sboot.api.dental_clinic_api.mapper.TreatmentPlanContractMapper;
import com.sboot.api.dental_clinic_api.mapper.TreatmentPlanMapper;
import com.sboot.api.dental_clinic_api.mapper.TreatmentPlanProcedureMapper;
import com.sboot.api.dental_clinic_api.mapper.TreatmentPlanTermsMapper;
import com.sboot.api.dental_clinic_api.repository.*;
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
public class TreatmentPlanService {

    public static final String AUTOMATICO = "Automático";
    private final TreatmentPlanRepository repository;
    private final TreatmentPlanProcedureRepository procedureRepository;
    private final TreatmentPlanTermsRepository termsRepository;
    private final TreatmentPlanContractRepository contractRepository;
    private final PatientProcedureRepository patientProcedureRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final TreatmentPlanMapper mapper;
    private final TreatmentPlanProcedureMapper procedureMapper;
    private final TreatmentPlanTermsMapper termsMapper;
    private final TreatmentPlanContractMapper contractMapper;
    private final ContractContentGenerator contractContentGenerator;

    public Page<TreatmentPlanResponseDTO> findPageAll(int page, int size, String search) {
        log.debug("Retrieving treatment plans with pagination: page={}, size={}, search={}", page, size, search);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<TreatmentPlan> result = repository.findAllByFilters(search, pageable);
        log.debug("Found {} treatment plans", result.getTotalElements());
        return result.map(mapper::toResponseDTO);
    }

    public TreatmentPlanResponseDTO findById(String id) {
        log.debug("Retrieving treatment plan by ID: {}", id);
        TreatmentPlan treatmentPlan = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("TreatmentPlan not found with ID: {}", id);
                    return new RuntimeException("TreatmentPlan not found with id: " + id);
                });
        return mapper.toResponseDTO(treatmentPlan);
    }

    public List<TreatmentPlanResponseDTO> findByPatientId(String patientId) {
        log.debug("Retrieving treatment plans for patient ID: {}", patientId);
        List<TreatmentPlan> result = repository.findByPatientId(patientId);
        log.debug("Found {} treatment plans for patient ID: {}", result.size(), patientId);
        return result.stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    public Page<BudgetDTO> getBudgetsByPatientId(String patientId, int page, int size) {
        log.debug("Retrieving budgets for patient ID: {} with pagination: page={}, size={}", patientId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<TreatmentPlan> treatmentPlans = repository.findByPatientId(patientId, pageable);
        log.debug("Found {} budgets for patient ID: {}", treatmentPlans.getTotalElements(), patientId);
        return treatmentPlans.map(tp -> {
            BudgetDTO budget = mapper.toBudgetDTO(tp);
            budget.setProcedures(mapper.mapBudgetProcedures(tp.getProcedures()));
            return budget;
        });
    }

    @Transactional
    public TreatmentPlanResponseDTO create(TreatmentPlanRequestDTO request) {
        log.info("Creating new treatment plan for patient ID: {}", request.getPatientId());
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

        entity.setCreatedAt(LocalDateTime.now());
        entity.setValidUntil(LocalDate.now().plusMonths(3));

        TreatmentPlan saved = repository.save(entity);
        log.info("Successfully created treatment plan with ID: {}", saved.getId());
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    public TreatmentPlanResponseDTO update(String id, TreatmentPlanRequestDTO request) {
        log.info("Updating treatment plan with ID: {}", id);
        TreatmentPlan existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("TreatmentPlan not found with ID: {}", id);
                    return new RuntimeException("TreatmentPlan not found with id: " + id);
                });

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
        existing.setStatus(TreatmentPlanStatus.waiting_signature);

        if (request.getProcedures() != null) {
            procedureRepository.deleteByTreatmentPlanId(id);

            List<TreatmentPlanProcedure> procedures = request.getProcedures().stream()
                    .map(procedureMapper::toEntity)
                    .collect(Collectors.toList());
            procedures.forEach(p -> p.setTreatmentPlan(existing));
            existing.setProcedures(procedures);
        }

        termsRepository.deleteByTreatmentPlanId(existing.getId());
        contractRepository.deleteByTreatmentPlanId(existing.getId());

        existing.setTerms(null);
        existing.setContract(null);

        TreatmentPlan saved = repository.save(existing);

        if (saved.getStatus() == TreatmentPlanStatus.approved && saved.getProcedures() != null && !saved.getProcedures().isEmpty()) {
            updatePatientProceduresFromTreatmentPlan(saved);
        }

        log.info("Successfully updated treatment plan with ID: {}", id);
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    private void updatePatientProceduresFromTreatmentPlan(TreatmentPlan treatmentPlan) {
        log.debug("Updating patient procedures from treatment plan ID: {}", treatmentPlan.getId());
        
        deleteExistingPatientProcedures(treatmentPlan);
        
        BigDecimal discountRatio = calculateDiscountRatio(treatmentPlan);
        
        List<PatientProcedure> patientProcedures = createPatientProcedures(treatmentPlan, discountRatio);
        
        adjustProcedureValuesIfNeeded(patientProcedures, treatmentPlan);
        
        savePatientProcedures(patientProcedures, treatmentPlan.getId());
    }

    private void deleteExistingPatientProcedures(TreatmentPlan treatmentPlan) {
        log.debug("Deleting existing patient procedures for treatment plan ID: {}", treatmentPlan.getId());
        
        List<PatientProcedure> existingProcedures = patientProcedureRepository.findByTreatmentPlanId(treatmentPlan.getId());
        
        for (PatientProcedure procedure : existingProcedures) {
            financialTransactionRepository.deleteByPatientProcedureId(procedure.getId());
        }
        
        patientProcedureRepository.deleteByTreatmentPlanId(treatmentPlan.getId());
    }

    private BigDecimal calculateDiscountRatio(TreatmentPlan treatmentPlan) {
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
        
        return discountRatio;
    }

    private List<PatientProcedure> createPatientProcedures(TreatmentPlan treatmentPlan, BigDecimal discountRatio) {
        List<PatientProcedure> patientProcedures = new java.util.ArrayList<>();

        for (TreatmentPlanProcedure procedure : treatmentPlan.getProcedures()) {
            PatientProcedure patientProcedure = mapToPatientProcedure(procedure, treatmentPlan, discountRatio);
            patientProcedures.add(patientProcedure);
        }
        
        return patientProcedures;
    }

    private PatientProcedure mapToPatientProcedure(TreatmentPlanProcedure procedure, TreatmentPlan treatmentPlan, BigDecimal discountRatio) {
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
        
        return patientProcedure;
    }

    private void adjustProcedureValuesIfNeeded(List<PatientProcedure> patientProcedures, TreatmentPlan treatmentPlan) {
        if (patientProcedures.isEmpty()) {
            return;
        }

        BigDecimal totalDiscountedValue = patientProcedures.stream()
                .map(PatientProcedure::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal targetTotal = treatmentPlan.getFinalValue() != null ? treatmentPlan.getFinalValue() : BigDecimal.ZERO;
        BigDecimal difference = targetTotal.subtract(totalDiscountedValue);

        if (difference.abs().compareTo(new BigDecimal("0.01")) > 0) {
            PatientProcedure lastProcedure = patientProcedures.get(patientProcedures.size() - 1);
            BigDecimal adjustedValue = lastProcedure.getValue().add(difference);
            lastProcedure.setValue(adjustedValue.setScale(2, java.math.RoundingMode.HALF_UP));
        }
    }

    private void savePatientProcedures(List<PatientProcedure> patientProcedures, String treatmentPlanId) {
        for (PatientProcedure patientProcedure : patientProcedures) {
            patientProcedureRepository.save(patientProcedure);
        }
        log.debug("Created {} patient procedures from treatment plan ID: {}", patientProcedures.size(), treatmentPlanId);
    }

    @Transactional
    public void delete(String id) {
        log.info("Deleting treatment plan with ID: {}", id);
        TreatmentPlan treatmentPlan = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("TreatmentPlan not found with ID: {}", id);
                    return new RuntimeException("TreatmentPlan not found with id: " + id);
                });

        if (treatmentPlan.getProcedures() != null) {
            for (TreatmentPlanProcedure procedure : treatmentPlan.getProcedures()) {
                patientProcedureRepository.deleteByTreatmentPlanIdAndProcedureClinicId(id, procedure.getProcedureClinic().getId());
            }
        }

        repository.deleteById(id);
        log.info("Successfully deleted treatment plan with ID: {}", id);
    }

    @Transactional
    public TreatmentPlanResponseDTO updateStatus(String id, TreatmentPlanStatus status) {
        log.info("Updating status for treatment plan ID: {} to status: {}", id, status);
        TreatmentPlan treatmentPlan = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("TreatmentPlan not found with ID: {}", id);
                    return new RuntimeException("TreatmentPlan not found with id: " + id);
                });

        treatmentPlan.setStatus(status);
        TreatmentPlan saved = repository.save(treatmentPlan);

        if (status == TreatmentPlanStatus.approved && saved.getProcedures() != null && !saved.getProcedures().isEmpty()) {
            updatePatientProceduresFromTreatmentPlan(saved);
        }

        log.info("Successfully updated status for treatment plan ID: {} to status: {}", id, status);
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    public TreatmentPlanResponseDTO saveContractAndTerms(String treatmentPlanId, TreatmentPlanTermsDTO termsDTO, TreatmentPlanContractDTO contractDTO) {
        log.info("Saving contract and terms for treatment plan ID: {}", treatmentPlanId);
        TreatmentPlan treatmentPlan = repository.findById(treatmentPlanId)
                .orElseThrow(() -> {
                    log.error("TreatmentPlan not found with ID: {}", treatmentPlanId);
                    return new RuntimeException("TreatmentPlan not found with id: " + treatmentPlanId);
                });

        termsRepository.deleteByTreatmentPlanId(treatmentPlan.getId());
        contractRepository.deleteByTreatmentPlanId(treatmentPlan.getId());
        treatmentPlan.setTerms(null);
        treatmentPlan.setContract(null);

        if (termsDTO != null) {
            TreatmentPlanTerms terms = termsMapper.toEntity(termsDTO);
            terms.setTreatmentPlan(treatmentPlan);

            termsRepository.save(terms);
            treatmentPlan.setTerms(terms);
        }

        if (contractDTO != null) {
            TreatmentPlanContract contract = contractMapper.toEntity(contractDTO);
            contract.setTreatmentPlan(treatmentPlan);

            if (contract.getContractContent() == null || contract.getContractContent().isEmpty()) {
                String contractContent = contractContentGenerator.generateContractContent(treatmentPlan, contract);
                contract.setContractContent(contractContent);
            }

            contractRepository.save(contract);
            treatmentPlan.setContract(contract);

            if (!Boolean.TRUE.equals(contractDTO.getSigned())) {
                treatmentPlan.setStatus(TreatmentPlanStatus.waiting_signature);
            } else {
                treatmentPlan.setStatus(TreatmentPlanStatus.signed);
            }
        }

        TreatmentPlan saved = repository.save(treatmentPlan);
        log.info("Successfully saved contract and terms for treatment plan ID: {}", treatmentPlanId);
        return mapper.toResponseDTO(saved);
    }
}