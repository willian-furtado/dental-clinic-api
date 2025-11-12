package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.AnamnesisQuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnamnesisQuestionOptionRepository extends JpaRepository<AnamnesisQuestionOption, String> {

    List<AnamnesisQuestionOption> findByQuestionId(String questionId);
}