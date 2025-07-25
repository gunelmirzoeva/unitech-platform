package com.unitech.account.dto;

import lombok.Data;
import java.math.BigDecimal;

// This DTO is for internal service-to-service communication
@Data
public class InternalTransferRequest {
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
}