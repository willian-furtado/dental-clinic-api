package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConditionalTextDTO {

    private String triggerValue;

    private String placeholder;

    private String label;
}
