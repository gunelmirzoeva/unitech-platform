package com.unitech.transfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountDto {
    private UUID id;
    @NotBlank(message = "IBAN is required")
    private String iban;
    @DecimalMin(value = "0.00", message = "Balance must be non-negative")
    private BigDecimal balance;
}