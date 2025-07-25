package com.unitech.account.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateAccountRequest {
    @NotNull(message = "User ID cannot be null")
    private UUID userId;

    @NotNull(message = "Initial balance cannot be null")
    private BigDecimal initialBalance;

    @NotNull(message = "Currency cannot be null")
    private String currency;
}