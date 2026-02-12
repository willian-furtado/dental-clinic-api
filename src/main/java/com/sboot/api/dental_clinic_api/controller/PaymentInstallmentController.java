package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.PaymentInstallmentDTO;
import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.PaymentStatus;
import com.sboot.api.dental_clinic_api.service.PaymentInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-installments")
public class PaymentInstallmentController {

    @Autowired
    private PaymentInstallmentService paymentInstallmentService;

    @GetMapping("/treatment-plan/{treatmentPlanId}")
    public ResponseEntity<List<PaymentInstallmentDTO>> getInstallmentsByTreatmentPlan(
            @PathVariable String treatmentPlanId) {
        List<PaymentInstallmentDTO> installments = paymentInstallmentService.getInstallmentsByTreatmentPlan(treatmentPlanId);
        return ResponseEntity.ok(installments);
    }

    @GetMapping("/by-treatment-plan/{treatmentPlanId}")
    public ResponseEntity<List<PaymentInstallmentDTO>> getInstallmentsByTreatmentPlanId(
            @PathVariable String treatmentPlanId) {
        List<PaymentInstallmentDTO> installments = paymentInstallmentService.getInstallmentsByTreatmentPlan(treatmentPlanId);
        return ResponseEntity.ok(installments);
    }

    @PostMapping("/create/{treatmentPlanId}")
    public ResponseEntity<List<PaymentInstallmentDTO>> createInstallments(
            @PathVariable String treatmentPlanId,
            @RequestParam Integer installments,
            @RequestParam String paymentMethod) {
        
        PaymentMethod method = PaymentMethod.valueOf(paymentMethod);
        List<PaymentInstallmentDTO> createdInstallments = paymentInstallmentService.createInstallments(treatmentPlanId, installments, method);
        return ResponseEntity.ok(createdInstallments);
    }

    @PutMapping("/treatment-plan/{treatmentPlanId}/update")
    public ResponseEntity<List<PaymentInstallmentDTO>> updateInstallments(
            @PathVariable String treatmentPlanId,
            @RequestParam Integer installments,
            @RequestParam String paymentMethod) {
        
        PaymentMethod method = PaymentMethod.valueOf(paymentMethod);
        List<PaymentInstallmentDTO> updatedInstallments = paymentInstallmentService.updateInstallments(treatmentPlanId, installments, method);
        return ResponseEntity.ok(updatedInstallments);
    }

    @PutMapping("/{installmentId}/status")
    public ResponseEntity<PaymentInstallmentDTO> updateInstallmentStatus(
            @PathVariable String installmentId,
            @RequestParam String status) {
        
        PaymentStatus paymentStatus = PaymentStatus.valueOf(status);
        PaymentInstallmentDTO updatedInstallment = paymentInstallmentService.updateInstallmentStatus(installmentId, paymentStatus);
        return ResponseEntity.ok(updatedInstallment);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<PaymentInstallmentDTO>> getOverdueInstallments() {
        List<PaymentInstallmentDTO> overdueInstallments = paymentInstallmentService.getOverdueInstallments();
        return ResponseEntity.ok(overdueInstallments);
    }

    @GetMapping("/due")
    public ResponseEntity<List<PaymentInstallmentDTO>> getDueInstallments() {
        List<PaymentInstallmentDTO> dueInstallments = paymentInstallmentService.getDueInstallments();
        return ResponseEntity.ok(dueInstallments);
    }
}