package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.AuthResponseDTO;
import com.sboot.api.dental_clinic_api.dto.LoginRequestDTO;
import com.sboot.api.dental_clinic_api.dto.RefreshTokenRequestDTO;
import com.sboot.api.dental_clinic_api.entity.User;
import com.sboot.api.dental_clinic_api.repository.UserRepository;
import com.sboot.api.dental_clinic_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            AuthResponseDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequest) {
        AuthResponseDTO response = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
