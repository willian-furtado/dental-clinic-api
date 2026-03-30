package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.UpdateUserRequestDTO;
import com.sboot.api.dental_clinic_api.dto.UserDTO;
import com.sboot.api.dental_clinic_api.entity.Clinic;
import com.sboot.api.dental_clinic_api.entity.ClinicAddress;
import com.sboot.api.dental_clinic_api.entity.User;
import com.sboot.api.dental_clinic_api.mapper.UserMapper;
import com.sboot.api.dental_clinic_api.repository.ClinicRepository;
import com.sboot.api.dental_clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDTO updateUser(String userId, UpdateUserRequestDTO request) {
        log.info("Updating user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getCpf() != null) user.setCpf(request.getCpf());
        if (request.getBirthDate() != null) user.setBirthDate(request.getBirthDate());
        if (request.getCro() != null) user.setCro(request.getCro());
        if (request.getCroState() != null) user.setCroState(request.getCroState());
        if (request.getSpecialty() != null) user.setSpecialty(request.getSpecialty());

        if (request.getClinic() != null && user.getClinic() != null) {
            updateClinic(user.getClinic(), request.getClinic());
        }

        user = userRepository.save(user);
        log.info("User updated successfully: {}", userId);

        return userMapper.toDto(user);
    }

    private void updateClinic(Clinic clinic, UpdateUserRequestDTO.UpdateClinicRequestDTO request) {
        if (request.getName() != null) clinic.setName(request.getName());
        if (request.getPhone() != null) clinic.setPhone(request.getPhone());
        if (request.getEmail() != null) clinic.setEmail(request.getEmail());
        if (request.getCnpj() != null) clinic.setCnpj(request.getCnpj());

        if (request.getAddress() == null) return;

        ClinicAddress address = clinic.getAddress() != null ? clinic.getAddress() : new ClinicAddress();
        clinic.setAddress(address);

        UpdateUserRequestDTO.UpdateClinicAddressRequestDTO addressRequest = request.getAddress();
        if (addressRequest.getCep() != null) address.setCep(addressRequest.getCep());
        if (addressRequest.getStreet() != null) address.setStreet(addressRequest.getStreet());
        if (addressRequest.getNumber() != null) address.setNumber(addressRequest.getNumber());
        if (addressRequest.getComplement() != null) address.setComplement(addressRequest.getComplement());
        if (addressRequest.getNeighborhood() != null) address.setNeighborhood(addressRequest.getNeighborhood());
        if (addressRequest.getCity() != null) address.setCity(addressRequest.getCity());
        if (addressRequest.getState() != null) address.setState(addressRequest.getState());

        clinicRepository.save(clinic);
    }
}
