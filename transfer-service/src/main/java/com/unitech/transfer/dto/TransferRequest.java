package com.unitech.transfer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferRequest {

    @NotNull
    @PositiveOrZero(message = "Sender ID must be positive")
    private Long senderAccountId;

    @NotNull
    @PositiveOrZero(message = "Receiver ID must be positive")
    private Long receiverAccountId;

    @NotNull
    private String idempotencyKey;

    @NotNull
    @Min(value = 1, message = "Amount must be at least 1")
    private BigDecimal amount;
}
