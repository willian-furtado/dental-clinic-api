package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamnesisResponseDTO {

    private String id;

    private String questionId;

    private String selectedOptionId;

    private String value;

    private String extraText;

}
