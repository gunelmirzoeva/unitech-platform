package com.unitech.currency.dtos;

import lombok.Data;

@Data
public class ConversionRequest {
    private String fromCurrency;
    private String toCurrency;
    private double amount;
}