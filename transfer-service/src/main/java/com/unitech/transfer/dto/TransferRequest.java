package com.unitech.transfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotBlank
    private String fromIban;
    @NotBlank
    private String toIban;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}