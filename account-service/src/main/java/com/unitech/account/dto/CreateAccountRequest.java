package com.unitech.account.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateAccountRequest {
    private UUID userId;
    private BigDecimal initialBalance;
    private String currency;
}