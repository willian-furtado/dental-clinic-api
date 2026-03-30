package com.sboot.api.dental_clinic_api.dto;

import lombok.Data;

@Data
public class ClinicAddressDTO {
    private String id;
    private String cep;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
}