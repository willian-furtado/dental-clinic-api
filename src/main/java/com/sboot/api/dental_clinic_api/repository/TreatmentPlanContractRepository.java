package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.TreatmentPlanContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentPlanContractRepository extends JpaRepository<TreatmentPlanContract, String> {

}
