package com.sboot.api.dental_clinic_api.dto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureClinicRequestDTO {
    private String name;
    private String category;
    private String description;
    private BigDecimal basePrice;
    private Integer duration;
    private Boolean isActive;
    private String createdAt;
}
