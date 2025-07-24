package com.unitech.account.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateBalanceRequest {

    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;
}