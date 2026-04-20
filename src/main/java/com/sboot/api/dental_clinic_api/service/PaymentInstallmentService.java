package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.PaymentInstallmentDTO;
import com.sboot.api.dental_clinic_api.entity.FinancialTransaction;
import com.sboot.api.dental_clinic_api.entity.PatientProcedure;
import com.sboot.api.dental_clinic_api.entity.PaymentInstallment;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlan;
import com.sboot.api.dental_clinic_api.enums.IncomeSource;
import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.PaymentStatus;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import com.sboot.api.dental_clinic_api.mapper.PaymentInstallmentMapper;
import com.sboot.api.dental_clinic_api.repository.FinancialTransactionRepository;
import com.sboot.api.dental_clinic_api.repository.PatientProcedureRepository;
import com.sboot.api.dental_clinic_api.repository.PaymentInstallmentRepository;
import com.sboot.api.dental_clinic_api.repository.TreatmentPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentInstallmentService {

    private final PaymentInstallmentRepository paymentInstallmentRepository;

    private final TreatmentPlanRepository treatmentPlanRepository;

    private final PatientProcedureRepository patientProcedureRepository;

    private final FinancialTransactionRepository financialTransactionRepository;

    private final PaymentInstallmentMapper paymentInstallmentMapper;

    @Transactional
    public void createInstallments(String patientProcedureId, Integer installments, PaymentMethod paymentMethod) {
        log.info("Creating installments for patient procedure ID: {}, installments: {}, payment method: {}", patientProcedureId, installments, paymentMethod);
        
        PatientProcedure patientProcedure = patientProcedureRepository.findById(patientProcedureId)
                .orElseThrow(() -> {
                    log.error("Patient procedure not found with ID: {}", patientProcedureId);
                    return new RuntimeException("Patient procedure not found");
                });

        List<PaymentInstallment> installmentsList = paymentInstallmentRepository.findByPatientProcedureId(patientProcedureId);
        
        if (!installmentsList.isEmpty()) {
            log.debug("Deleting {} existing installments for patient procedure ID: {}", installmentsList.size(), patientProcedureId);
            paymentInstallmentRepository.deleteAll(installmentsList);
        }

        LocalDate currentDate = LocalDate.now();
        
        PaymentInstallment installment = PaymentInstallment.builder()
                .patientProcedure(patientProcedure)
                .dueDate(currentDate)
                .amount(patientProcedure.getValue())
                .paymentMethod(paymentMethod)
                .installmentNumber(installments)
                .status(PaymentStatus.pending)
                .createdAt(LocalDateTime.now())
                .build();
        
        paymentInstallmentRepository.save(installment);
        
        createFinancialTransactionFromPatientProcedure(patientProcedure, paymentMethod);
        
        log.info("Successfully created installment for patient procedure ID: {}", patientProcedureId);
    }

    private void createFinancialTransactionFromPatientProcedure(PatientProcedure patientProcedure, PaymentMethod paymentMethod) {
        log.debug("Creating financial transaction for patient procedure ID: {}", patientProcedure.getId());
        
        FinancialTransaction transaction = FinancialTransaction.builder()
                .type(TransactionType.INCOME)
                .description(patientProcedure.getProcedureClinic() != null ? patientProcedure.getProcedureClinic().getName() : "N/A")
                .amount(patientProcedure.getValue())
                .date(LocalDate.now())
                .paymentMethod(paymentMethod)
                .createdAt(LocalDateTime.now())
                .source(patientProcedure.getOrigin().equals("Manual") ? IncomeSource.appointment : IncomeSource.procedure)
                .patientProcedure(patientProcedure)
                .patient(patientProcedure.getPatient())
                .status(PaymentStatus.paid)
                .build();
        
        financialTransactionRepository.save(transaction);
        log.debug("Successfully created financial transaction for patient procedure ID: {}", patientProcedure.getId());
    }

    @Transactional
    public List<PaymentInstallmentDTO> updateInstallments(String patientProcedureId, Integer installments, PaymentMethod paymentMethod) {
        log.info("Updating installments for patient procedure ID: {}, installments: {}, payment method: {}", patientProcedureId, installments, paymentMethod);
        
        PatientProcedure patientProcedure = patientProcedureRepository.findById(patientProcedureId)
                .orElseThrow(() -> {
                    log.error("Patient procedure not found with ID: {}", patientProcedureId);
                    return new RuntimeException("Patient procedure not found");
                });

        LocalDate currentDate = LocalDate.now();
        
        List<PaymentInstallment> existingInstallments = paymentInstallmentRepository.findByPatientProcedureId(patientProcedureId);
        if (!existingInstallments.isEmpty()) {
            log.debug("Deleting {} existing installments for patient procedure ID: {}", existingInstallments.size(), patientProcedureId);
            paymentInstallmentRepository.deleteAll(existingInstallments);
        }

        // Create only one installment regardless of the installments count
        PaymentInstallment installment = PaymentInstallment.builder()
                .patientProcedure(patientProcedure)
                .dueDate(currentDate)
                .amount(patientProcedure.getValue())
                .paymentMethod(paymentMethod)
                .installmentNumber(1)
                .status(PaymentStatus.pending)
                .createdAt(LocalDateTime.now())
                .build();
        
        paymentInstallmentRepository.save(installment);
        log.info("Successfully updated installment for patient procedure ID: {}", patientProcedureId);

        return paymentInstallmentRepository.findByPatientProcedureId(patientProcedureId)
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentInstallmentDTO> getInstallmentsByPatientProcedure(String patientProcedureId) {
        log.debug("Retrieving installments for patient procedure ID: {}", patientProcedureId);
        List<PaymentInstallmentDTO> result = paymentInstallmentRepository.findByPatientProcedureId(patientProcedureId)
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
        log.debug("Found {} installments for patient procedure ID: {}", result.size(), patientProcedureId);
        return result;
    }

    @Transactional
    public PaymentInstallmentDTO updateInstallmentStatus(String installmentId, PaymentStatus status) {
        log.info("Updating status for installment ID: {} to status: {}", installmentId, status);
        
        PaymentInstallment installment = paymentInstallmentRepository.findById(installmentId)
                .orElseThrow(() -> {
                    log.error("Installment not found with ID: {}", installmentId);
                    return new RuntimeException("Installment not found");
                });

        installment.setStatus(status);
        if (status == PaymentStatus.paid) {
            installment.setPaidAt(LocalDateTime.now());
            log.debug("Setting paidAt timestamp for installment ID: {}", installmentId);
        } else {
            installment.setPaidAt(null);
        }

        PaymentInstallment updatedInstallment = paymentInstallmentRepository.save(installment);
        log.info("Successfully updated status for installment ID: {} to status: {}", installmentId, status);
        return paymentInstallmentMapper.toDTO(updatedInstallment);
    }

    public List<PaymentInstallmentDTO> getOverdueInstallments() {
        log.debug("Retrieving overdue installments");
        List<PaymentInstallmentDTO> result = paymentInstallmentRepository.findOverdueInstallments(LocalDate.now())
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
        log.debug("Found {} overdue installments", result.size());
        return result;
    }

    public List<PaymentInstallmentDTO> getDueInstallments() {
        log.debug("Retrieving due installments");
        List<PaymentInstallmentDTO> result = paymentInstallmentRepository.findDueInstallments(LocalDate.now())
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
        log.debug("Found {} due installments", result.size());
        return result;
    }
}