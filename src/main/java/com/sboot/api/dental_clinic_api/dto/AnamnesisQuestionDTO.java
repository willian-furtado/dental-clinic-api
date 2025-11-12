package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamnesisQuestionDTO {
    private String id;

    private String text;

    private String type;

    private Boolean required;

    private String category;

    private String createdAt;

    private String updatedAt;

    private List<AnamnesisQuestionOptionDTO> options;

    private ConditionalTextDTO conditionalText;

}
