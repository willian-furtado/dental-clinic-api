package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.TreatmentPlanRequestDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanResponseDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanProcedureDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanTermsDTO;
import com.sboot.api.dental_clinic_api.dto.TreatmentPlanContractDTO;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlan;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlanProcedure;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlanTerms;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlanContract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TreatmentPlanMapper {

    public TreatmentPlanResponseDTO toResponseDTO(TreatmentPlan entity) {
        if (entity == null) return null;

        TreatmentPlanResponseDTO dto = new TreatmentPlanResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(String.valueOf(entity.getCode()));
        dto.setPatientId(entity.getPatient() != null ? entity.getPatient().getId() : null);
        dto.setPatientName(entity.getPatient() != null ? entity.getPatient().getName() : null);
        dto.setSubtotal(entity.getSubtotal());
        dto.setTotal(entity.getTotal());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        dto.setValidUntil(entity.getValidUntil());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setInstallments(entity.getInstallments());
        dto.setFinalValue(entity.getFinalValue());
        dto.setPaymentDiscount(entity.getPaymentDiscount());
        dto.setPaymentDiscountType(entity.getPaymentDiscountType());
        dto.setPaymentDiscountAmount(entity.getPaymentDiscountAmount());
        dto.setCreatedAt(entity.getCreatedAt());

        dto.setProcedures(mapProcedures(entity.getProcedures()));
        dto.setTerms(mapTerms(entity.getTerms(), entity.getContract()));
        dto.setContract(mapContract(entity.getContract()));

        return dto;
    }

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "procedures", ignore = true)
    @Mapping(target = "terms", ignore = true)
    @Mapping(target = "contract", ignore = true)
    public abstract TreatmentPlan toEntity(TreatmentPlanRequestDTO dto);

    protected List<TreatmentPlanProcedureDTO> mapProcedures(List<TreatmentPlanProcedure> procedures) {
        if (procedures == null) return null;
        return procedures.stream()
                .map(p -> {
                    TreatmentPlanProcedureDTO dto = new TreatmentPlanProcedureDTO();
                    dto.setId(p.getId());
                    dto.setProcedureId(p.getProcedureClinic() != null ? p.getProcedureClinic().getId() : null);
                    dto.setToothNumber(p.getToothNumber());
                    String faces = p.getFaces();
                    dto.setFaces(faces != null && !faces.isEmpty() ? Arrays.asList(faces.split(",")) : null);
                    dto.setProcedure(p.getProcedureClinic().getName());
                    dto.setValue(p.getValue());
                    dto.setNotes(p.getNotes());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    protected TreatmentPlanTermsDTO mapTerms(TreatmentPlanTerms terms, TreatmentPlanContract contract) {
        if (terms == null) return null;
        TreatmentPlanTermsDTO dto = new TreatmentPlanTermsDTO();
        dto.setId(terms.getId());
        dto.setSigned(terms.getSigned());
        dto.setSignedAt(terms.getSignedAt());
        dto.setSignedBy(terms.getSignedBy());
        dto.setTermsContent(terms.getTermsContent());
        dto.setSignatureImage(contract.getSignatureImage());
        return dto;
    }

    protected TreatmentPlanContractDTO mapContract(TreatmentPlanContract contract) {
        if (contract == null) return null;
        TreatmentPlanContractDTO dto = new TreatmentPlanContractDTO();
        dto.setId(contract.getId());
        dto.setSigned(contract.getSigned());
        dto.setSignedAt(contract.getSignedAt());
        dto.setSignedBy(contract.getSignedBy());
        dto.setContractContent(contract.getContractContent());
        dto.setContractorSignature(contract.getContractorSignature());
        dto.setContractorSignedAt(contract.getContractorSignedAt());
        dto.setContractorSignedBy(contract.getContractorSignedBy());
        dto.setContractorSignatureImage(contract.getContractorSignatureImage());
        dto.setSignatureImage(contract.getSignatureImage());
        return dto;
    }
}
