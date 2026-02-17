package com.sboot.api.dental_clinic_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractGenerationRequestDTO {

    private String patientId;

    private BigDecimal totalValue;

    private String paymentMethod;

    private Boolean isPaid;
}
