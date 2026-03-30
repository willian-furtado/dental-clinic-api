package com.sboot.api.dental_clinic_api.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String cpf;
    private String birthDate;
    private String cro;
    private String croState;
    private String specialty;
    private ClinicDTO clinic;
}
