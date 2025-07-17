package com.unitech.auth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record RegisterRequest (
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    String email,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least 1 digit, 1 lowercase, 1 uppercase, and 1 special character"
    )
    String password,

    @NotBlank(message = "Password confirmation is required")
    String confirmPassword

) {
        @AssertTrue(message = "Passwords must match")
        public boolean isPasswordMatch() {
            return password != null && password.equals(confirmPassword);
        }
}
