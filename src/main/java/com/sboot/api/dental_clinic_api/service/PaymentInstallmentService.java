package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.PaymentInstallmentDTO;
import com.sboot.api.dental_clinic_api.entity.PaymentInstallment;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlan;
import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.PaymentStatus;
import com.sboot.api.dental_clinic_api.mapper.PaymentInstallmentMapper;
import com.sboot.api.dental_clinic_api.repository.PaymentInstallmentRepository;
import com.sboot.api.dental_clinic_api.repository.TreatmentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentInstallmentService {

    private final PaymentInstallmentRepository paymentInstallmentRepository;

    private final TreatmentPlanRepository treatmentPlanRepository;

    private final PaymentInstallmentMapper paymentInstallmentMapper;

    @Transactional
    public List<PaymentInstallmentDTO> createInstallments(String treatmentPlanId, Integer installments, PaymentMethod paymentMethod) {
        TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(treatmentPlanId)
                .orElseThrow(() -> new RuntimeException("Treatment plan not found"));

        LocalDate currentDate = LocalDate.now();
        
        List<PaymentInstallment> installmentsList = paymentInstallmentRepository.findByTreatmentPlanId(treatmentPlanId);
        
        if (!installmentsList.isEmpty()) {
            paymentInstallmentRepository.deleteAll(installmentsList);
        }

        PaymentInstallment installment = PaymentInstallment.builder()
                .treatmentPlan(treatmentPlan)
                .dueDate(currentDate)
                .amount(treatmentPlan.getFinalValue())
                .paymentMethod(paymentMethod)
                .installmentNumber(installments)
                .status(PaymentStatus.pending)
                .createdAt(LocalDateTime.now())
                .build();
        
        paymentInstallmentRepository.save(installment);

        return paymentInstallmentRepository.findByTreatmentPlanId(treatmentPlanId)
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PaymentInstallmentDTO> updateInstallments(String treatmentPlanId, Integer installments, PaymentMethod paymentMethod) {
        TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(treatmentPlanId)
                .orElseThrow(() -> new RuntimeException("Treatment plan not found"));

        LocalDate currentDate = LocalDate.now();
        
        List<PaymentInstallment> existingInstallments = paymentInstallmentRepository.findByTreatmentPlanId(treatmentPlanId);
        if (!existingInstallments.isEmpty()) {
            paymentInstallmentRepository.deleteAll(existingInstallments);
        }

        // Create only one installment regardless of the installments count
        PaymentInstallment installment = PaymentInstallment.builder()
                .treatmentPlan(treatmentPlan)
                .dueDate(currentDate)
                .amount(treatmentPlan.getFinalValue())
                .paymentMethod(paymentMethod)
                .installmentNumber(1)
                .status(PaymentStatus.pending)
                .createdAt(LocalDateTime.now())
                .build();
        
        paymentInstallmentRepository.save(installment);

        return paymentInstallmentRepository.findByTreatmentPlanId(treatmentPlanId)
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentInstallmentDTO> getInstallmentsByTreatmentPlan(String treatmentPlanId) {
        return paymentInstallmentRepository.findByTreatmentPlanId(treatmentPlanId)
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentInstallmentDTO updateInstallmentStatus(String installmentId, PaymentStatus status) {
        PaymentInstallment installment = paymentInstallmentRepository.findById(installmentId)
                .orElseThrow(() -> new RuntimeException("Installment not found"));

        installment.setStatus(status);
        if (status == PaymentStatus.paid) {
            installment.setPaidAt(LocalDateTime.now());
        } else {
            installment.setPaidAt(null);
        }

        PaymentInstallment updatedInstallment = paymentInstallmentRepository.save(installment);
        return paymentInstallmentMapper.toDTO(updatedInstallment);
    }

    public List<PaymentInstallmentDTO> getOverdueInstallments() {
        return paymentInstallmentRepository.findOverdueInstallments(LocalDate.now())
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentInstallmentDTO> getDueInstallments() {
        return paymentInstallmentRepository.findDueInstallments(LocalDate.now())
                .stream()
                .map(paymentInstallmentMapper::toDTO)
                .collect(Collectors.toList());
    }
}