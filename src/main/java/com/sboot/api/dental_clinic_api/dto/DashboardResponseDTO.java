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
public class DashboardResponseDTO {

    private BigDecimal totalRevenues;

    private BigDecimal totalExpenses;

    private BigDecimal recurringExpenses;

    private BigDecimal nonRecurringExpenses;

    private BigDecimal netProfit;
}