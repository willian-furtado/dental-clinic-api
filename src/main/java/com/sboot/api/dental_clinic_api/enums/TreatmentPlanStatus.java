package com.sboot.api.dental_clinic_api.enums;

import lombok.Getter;
@Getter
public enum TreatmentPlanStatus {
    draft("Rascunho"),
    approved("Aprovado"),
    waiting_signature("Aguardando assinatura"),
    signed("Assinado"),
    rejected("Rejeitado"),
    completed("Concluído"),
    cancelled("Cancelado");

    @Getter
    private final String description;

    TreatmentPlanStatus(String description) {
        this.description = description;
    }
}
