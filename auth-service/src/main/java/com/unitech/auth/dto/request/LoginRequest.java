package com.unitech.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


public record LoginRequest (
    @Email(message = "Must be a valid email format (e.g., user@example.com)")
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    String email,

    @NotBlank(message = "Password cannot be blank")
    @Size(
            min = 8,
            max = 64,
            message = "Password must be 8-64 characters long"
    )
    String password
){}