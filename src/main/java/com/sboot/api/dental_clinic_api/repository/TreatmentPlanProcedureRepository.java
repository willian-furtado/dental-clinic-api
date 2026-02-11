package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.TreatmentPlanProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentPlanProcedureRepository extends JpaRepository<TreatmentPlanProcedure, String> {

    void deleteByTreatmentPlanId(String treatmentPlanId);
}
