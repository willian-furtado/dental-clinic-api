package com.sboot.api.dental_clinic_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;

    private String name;

    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    private String phone;
    private String email;

    @Column(name = "birth_date", nullable = false)
    private String birthDate;

    @Column(name = "photo", columnDefinition = "BYTEA")
    private byte[] photo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_consultation_date")
    private LocalDateTime lastConsultationDate;

    private String guardianName;

    private String guardianCpf;

    private String guardianPhone;

    private String guardianEmail;

    private String guardianRelationship;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private PatientAddress address;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientDocument> documents;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnamnesisResponse> anamnesisResponses;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PatientAnamnesis anamnesisHistory;
}
