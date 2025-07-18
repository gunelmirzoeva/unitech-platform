package com.unitech.transfer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferRequest {

    @NotNull
    private Long senderAccountId;

    @NotNull
    private Long receiverAccountId;

    @NotNull
    @Min(value = 1, message = "Amount must be at least 1")
    private BigDecimal amount;
}
