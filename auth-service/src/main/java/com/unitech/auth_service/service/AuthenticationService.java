package com.unitech.auth_service.service;

import com.unitech.auth_service.dto.AuthenticationResponse;
import com.unitech.auth_service.dto.LoginRequest;
import com.unitech.auth_service.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request);
}
