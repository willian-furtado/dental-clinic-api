package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.UpdateUserRequestDTO;
import com.sboot.api.dental_clinic_api.dto.UserDTO;
import com.sboot.api.dental_clinic_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable String id, @RequestBody UpdateUserRequestDTO request) {
        return userService.updateUser(id, request);
    }
}