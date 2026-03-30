package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.ClinicAddressDTO;
import com.sboot.api.dental_clinic_api.dto.ClinicDTO;
import com.sboot.api.dental_clinic_api.dto.UpdateUserRequestDTO;
import com.sboot.api.dental_clinic_api.dto.UserDTO;
import com.sboot.api.dental_clinic_api.entity.Clinic;
import com.sboot.api.dental_clinic_api.entity.ClinicAddress;
import com.sboot.api.dental_clinic_api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "clinic", source = "clinic")
    UserDTO toDto(User user);

    @Mapping(target = "address", source = "address")
    ClinicDTO clinicToDto(Clinic clinic);

    ClinicAddressDTO addressToDto(ClinicAddress address);

    @Mapping(target = "role", ignore = true)
    void updateUserFromDTO(UpdateUserRequestDTO dto, @MappingTarget User user);

    @Mapping(target = "address", source = "address")
    void updateClinicFromDTO(UpdateUserRequestDTO.UpdateClinicRequestDTO dto, @MappingTarget Clinic clinic);

    void updateAddressFromDTO(UpdateUserRequestDTO.UpdateClinicAddressRequestDTO dto, @MappingTarget ClinicAddress address);
}
