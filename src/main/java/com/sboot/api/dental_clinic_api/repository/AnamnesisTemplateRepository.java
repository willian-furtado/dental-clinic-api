package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.AnamnesisTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnamnesisTemplateRepository extends JpaRepository<AnamnesisTemplate, String> {

    List<AnamnesisTemplate> findByTargetAudience(String targetAudience);

    List<AnamnesisTemplate> findByIsActiveTrue();

    List<AnamnesisTemplate> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}