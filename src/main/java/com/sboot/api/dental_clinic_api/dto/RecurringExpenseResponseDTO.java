package com.sboot.api.dental_clinic_api.dto;

import com.sboot.api.dental_clinic_api.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringExpenseResponseDTO {

    private String id;

    private String description;

    private BigDecimal amount;

    private String category;

    private PaymentMethod paymentMethod;

    private String notes;

    private Integer dueDay;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}