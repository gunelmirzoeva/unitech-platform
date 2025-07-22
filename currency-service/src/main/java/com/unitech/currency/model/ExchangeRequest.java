package com.unitech.currency.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequest {

    @NotBlank
    private String from;

    @NotBlank
    private String to;

    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;
}
