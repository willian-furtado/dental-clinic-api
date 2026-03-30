package com.sboot.api.dental_clinic_api.dto;

import lombok.Data;

@Data
public class ClinicDTO {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String cnpj;
    private ClinicAddressDTO address;
}
