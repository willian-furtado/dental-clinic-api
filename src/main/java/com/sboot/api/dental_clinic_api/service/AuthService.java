package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.AuthResponseDTO;
import com.sboot.api.dental_clinic_api.dto.LoginRequestDTO;
import com.sboot.api.dental_clinic_api.dto.RefreshTokenRequestDTO;
import com.sboot.api.dental_clinic_api.dto.UserDTO;
import com.sboot.api.dental_clinic_api.entity.User;
import com.sboot.api.dental_clinic_api.mapper.UserMapper;
import com.sboot.api.dental_clinic_api.repository.UserRepository;
import com.sboot.api.dental_clinic_api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;

    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        log.info("Attempting login for user: {}", loginRequest.getUsername());
        
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseGet(() -> userRepository.findByEmail(loginRequest.getUsername())
                        .orElseThrow(() -> {
                            log.error("User not found during login: {}", loginRequest.getUsername());
                            return new UsernameNotFoundException("User not found: " + loginRequest.getUsername());
                        }));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", loginRequest.getUsername());
            throw new RuntimeException("Invalid credentials");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .disabled(!user.getEnabled())
                .build();

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        log.info("Login successful for user: {}", user.getUsername());
        return getAuthResponseDTO(user, accessToken, refreshToken);
    }

    private AuthResponseDTO getAuthResponseDTO(User user, String accessToken, String refreshToken) {
        UserDTO userDTO = userMapper.toDto(user);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtUtil.getAccessTokenExpiration());
        response.setUser(userDTO);
        return response;
    }


    public AuthResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        log.info("Attempting to refresh token");
        
        if (!jwtUtil.validateToken(refreshToken)) {
            log.warn("Invalid refresh token provided");
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        log.debug("Refreshing token for user: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        UserDetails userDetails = loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(jwtUtil.getAccessTokenExpiration());
        response.setUser(userMapper.toDto(user));
        
        log.info("Token refreshed successfully for user: {}", username);
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", maskSensitiveData(username));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", maskSensitiveData(username));
                    return new UsernameNotFoundException("User not found: " + username);
                });

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .disabled(!user.getEnabled())
                .build();
    }

    private String maskSensitiveData(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        
        if (data.contains("@")) {
            String[] parts = data.split("@");
            return parts.length == 2 
                    ? (parts[0].length() <= 2 ? "***" : parts[0].substring(0, 2) + "***") + "@" + parts[1]
                    : data;
        }
        
        return data.length() <= 2 ? "***" : data.substring(0, 2) + "***";
    }
}
