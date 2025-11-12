package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.PatientDocumentDTO;
import com.sboot.api.dental_clinic_api.entity.PatientDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(target = "byteData", source = "byteData", qualifiedByName = "bytesToBase64")
    PatientDocumentDTO toDto(PatientDocument patientDocument);

    @Mapping(target = "byteData", source = "byteData", qualifiedByName = "base64ToBytes")
    @Mapping(target = "uploadDate", expression = "java(java.time.LocalDateTime.now())")
    PatientDocument toEntity(PatientDocumentDTO patientDocument);

    @Named("bytesToBase64")
    default String bytesToBase64(byte[] bytes) {
        return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
    }

    @Named("base64ToBytes")
    default byte[] base64ToBytes(String base64) {
        if (base64 == null) return null;

        if (base64.contains(",")) {
            base64 = base64.substring(base64.indexOf(",") + 1);
        }
        return Base64.getDecoder().decode(base64);
    }
}
