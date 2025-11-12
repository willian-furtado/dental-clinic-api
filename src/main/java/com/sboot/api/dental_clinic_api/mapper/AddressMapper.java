package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.PatientAddressDTO;
import com.sboot.api.dental_clinic_api.entity.PatientAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    PatientAddressDTO toDto(PatientAddress patientAddress);

    PatientAddress toEntity(PatientAddressDTO patientAddressDTO);

}
