package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamnesisTemplateDTO {
    private String id;

    private String name;

    private String description;

    private String targetAudience;

    private Boolean isActive;

    private String createdAt;

    private String updatedAt;

    private List<AnamnesisQuestionDTO> questions;
}
