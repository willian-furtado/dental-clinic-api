package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.AnamnesisTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnamnesisTemplateRepository extends JpaRepository<AnamnesisTemplate, String> {

    @Query("SELECT template FROM AnamnesisTemplate template WHERE template.id = :id")
    AnamnesisTemplate findByAnamneseId(String id);

}