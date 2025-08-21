package com.sboot.api.dental_clinic_api.repository;
import com.sboot.api.dental_clinic_api.entity.AnamnesisQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnamnesisQuestionRepository extends JpaRepository<AnamnesisQuestion, String> {

    List<AnamnesisQuestion> findByTemplateId(String templateId);
}
