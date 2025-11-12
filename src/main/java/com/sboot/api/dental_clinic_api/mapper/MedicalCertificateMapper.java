package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.MedicalCertificateDTO;
import com.sboot.api.dental_clinic_api.entity.MedicalCertificate;
import com.sboot.api.dental_clinic_api.enums.CertificateType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalCertificateMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "certificateType", expression = "java(medicalCertificate.getCertificateType().name().toLowerCase())")
    MedicalCertificateDTO toDto(MedicalCertificate medicalCertificate);

    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "certificateType", expression = "java(mapCertificateType(medicalCertificateDTO.getCertificateType()))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    MedicalCertificate toEntity(MedicalCertificateDTO medicalCertificateDTO);

    default CertificateType mapCertificateType(String certificateType) {
        if (certificateType == null) return null;
        return CertificateType.fromValue(certificateType);
    }
}
