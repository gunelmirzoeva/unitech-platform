package com.unitech.account.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AccountResponse {
    private UUID id;
    private String iban;
    private UUID userId;
    private BigDecimal balance;
    private String currency;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
