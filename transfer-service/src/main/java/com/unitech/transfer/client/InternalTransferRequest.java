package com.unitech.transfer.client;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class InternalTransferRequest {
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
}