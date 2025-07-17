package com.unitech.auth.service;

import com.unitech.auth.dto.AuthResponse;
import com.unitech.auth.dto.request.RegisterRequest;
import com.unitech.auth.enums.Role;
import com.unitech.auth.model.User;
import com.unitech.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new IllegalArgumentException("Password and confirmation do not match");
        }

        validateRegistrationRequest(request);

        User user = createUserFromRequest(request);
        userRepository.save(user);

        return new AuthResponse("User registered successfully!");
    }
    private void validateRegistrationRequest(RegisterRequest request) {
        if(userRepository.existsByEmailOrUsername(request.getEmail(), request.getUsername())) {
            throw new RuntimeException("Email or username is already in use");
            //UserAlreadyExistsException("Email or username is already in use")
        }
    }
    private User createUserFromRequest(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
    }
}
