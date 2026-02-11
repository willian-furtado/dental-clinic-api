package com.sboot.api.dental_clinic_api.enums;

import lombok.Getter;
@Getter
public enum TreatmentPlanStatus {
    draft("Rascunho"),
    pending("Pendente"),
    approved("Aprovado"),
    rejected("Rejeitado"),
    converted("Convertido"),
    completed("Concluído"),
    cancelled("Cancelado");

    @Getter
    private final String description;

    TreatmentPlanStatus(String description) {
        this.description = description;
    }
}
