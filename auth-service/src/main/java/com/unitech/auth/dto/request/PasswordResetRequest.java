package com.unitech.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest (
        @NotBlank(message = "Email address is required")
        @Email(message = "Must be a valid email address (e.g., user@example.com)")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        String email
) {}
