package com.sboot.api.dental_clinic_api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CertificateType {
    DAYS,
    HOURS;

    @JsonValue
    public String getValue() {
        return this.name().toLowerCase();
    }

    @JsonCreator
    public static CertificateType fromValue(String value) {
        for (CertificateType type : CertificateType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CertificateType: " + value);
    }
}
