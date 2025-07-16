package com.unitech.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Must be a valid email format (e.g., user@example.com)")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(
            min = 8,
            max = 64,
            message = "Password must be 8-64 characters long"
    )
    private String password;
}