package com.unitech.currency.dtos;


import lombok.Data;

@Data
public class ConversionResponse {
    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double convertedAmount;
    private double exchangeRate;
}