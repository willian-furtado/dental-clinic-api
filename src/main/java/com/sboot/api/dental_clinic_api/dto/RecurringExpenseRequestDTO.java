package com.sboot.api.dental_clinic_api.dto;

import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringExpenseRequestDTO {

    private String description;

    private BigDecimal amount;

    private String category;

    private PaymentMethod paymentMethod;

    private String notes;

    private Integer dueDay;

    @Builder.Default
    private Boolean isActive = true;
}