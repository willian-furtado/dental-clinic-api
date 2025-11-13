package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuardianDTO {
    private String name;
    private String cpf;
    private String phone;
    private String email;
    private String relationship;
}
