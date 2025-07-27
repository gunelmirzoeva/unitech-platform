package com.unitech.transfer.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransferResponse {
    private UUID transactionId;
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
    private String status;
    private LocalDateTime timestamp;
}