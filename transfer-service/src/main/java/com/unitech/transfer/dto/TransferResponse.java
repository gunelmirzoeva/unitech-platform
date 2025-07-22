package com.unitech.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransferResponse {

    private Long id;
    private Long senderAccountId;
    private Long receiverAccountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String status;

}
