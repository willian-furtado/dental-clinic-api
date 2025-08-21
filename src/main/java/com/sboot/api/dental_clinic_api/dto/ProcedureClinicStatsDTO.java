package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureClinicStatsDTO {

    private Long totalProcedures;
    private Long activeProcedures;
    private BigDecimal averagePrice;
}
