package com.sboot.api.dental_clinic_api.dto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureClinicResponseDTO {
    private String id;
    private String name;
    private String category;
    private String description;
    private BigDecimal basePrice;
    private Integer duration;
    private Boolean isActive;
    private LocalDate createdAt;
}
