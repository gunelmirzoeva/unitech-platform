package com.unitech.currency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeResponse {

    private BigDecimal rate;
    private BigDecimal convertedAmount;
    private String currencyPair;
}
