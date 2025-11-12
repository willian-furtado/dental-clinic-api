package com.sboot.api.dental_clinic_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {

    private String id;

    private String date;

    private String time;

    private String patientId;

    private String patientName;

    private String type;

    private Integer duration;

    private String status;

    private String notes;

    private String createdAt;
}
