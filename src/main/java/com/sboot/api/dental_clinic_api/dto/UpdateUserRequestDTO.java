package com.sboot.api.dental_clinic_api.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String name;
    private String phone;
    private String cpf;
    private String birthDate;
    private String cro;
    private String croState;
    private String specialty;
    private UpdateClinicRequestDTO clinic;

    @Data
    public static class UpdateClinicRequestDTO {
        private String name;
        private String phone;
        private String email;
        private String cnpj;
        private UpdateClinicAddressRequestDTO address;
    }

    @Data
    public static class UpdateClinicAddressRequestDTO {
        private String cep;
        private String street;
        private String number;
        private String complement;
        private String neighborhood;
        private String city;
        private String state;
    }
}
