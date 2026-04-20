package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.PaymentInstallmentDTO;
import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import com.sboot.api.dental_clinic_api.enums.PaymentStatus;
import com.sboot.api.dental_clinic_api.service.PaymentInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/payment-installments")
public class PaymentInstallmentController {

    @Autowired
    private PaymentInstallmentService paymentInstallmentService;

    @GetMapping("/patient-procedure/{patientProcedureId}")
    public ResponseEntity<List<PaymentInstallmentDTO>> getInstallmentsByPatientProcedure(
            @PathVariable String patientProcedureId) {
        List<PaymentInstallmentDTO> installments = paymentInstallmentService.getInstallmentsByPatientProcedure(patientProcedureId);
        return ResponseEntity.ok(installments);
    }

    @GetMapping("/by-patient-procedure/{patientProcedureId}")
    public ResponseEntity<List<PaymentInstallmentDTO>> getInstallmentsByPatientProcedureId(
            @PathVariable String patientProcedureId) {
        List<PaymentInstallmentDTO> installments = paymentInstallmentService.getInstallmentsByPatientProcedure(patientProcedureId);
        return ResponseEntity.ok(installments);
    }

    @PostMapping("/create/{patientProcedureId}")
    public void createInstallments(
            @PathVariable String patientProcedureId,
            @RequestParam Integer installments,
            @RequestParam String paymentMethod) {
        
        PaymentMethod method = PaymentMethod.valueOf(paymentMethod);
        paymentInstallmentService.createInstallments(patientProcedureId, installments, method);
    }

    @PutMapping("/patient-procedure/{patientProcedureId}/update")
    public ResponseEntity<List<PaymentInstallmentDTO>> updateInstallments(
            @PathVariable String patientProcedureId,
            @RequestParam Integer installments,
            @RequestParam String paymentMethod) {
        
        PaymentMethod method = PaymentMethod.valueOf(paymentMethod);
        List<PaymentInstallmentDTO> updatedInstallments = paymentInstallmentService.updateInstallments(patientProcedureId, installments, method);
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