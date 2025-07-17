package com.unitech.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest (
        @NotBlank(message = "Refresh token is required")
        @Size(min = 128, max = 256, message = "Invalid refresh token length")
        @Pattern(
                regexp = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$",
                message = "Invalid JWT token format"
        )
        String refreshToken
)
{}
