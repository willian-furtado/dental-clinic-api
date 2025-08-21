package com.sboot.api.dental_clinic_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anamnesis_question_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamnesisQuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AnamnesisQuestion question;

    private String value;
}
