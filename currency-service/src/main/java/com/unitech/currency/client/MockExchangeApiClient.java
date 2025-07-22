package com.unitech.currency.client;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class MockExchangeApiClient {

    private static final Map<String, BigDecimal> rates = Map.of(
            "USD-AZN", new BigDecimal("1.7"),
            "EUR-AZN", new BigDecimal("1.85"),
            "USD-EUR", new BigDecimal("0.92"),
            "EUR-USD", new BigDecimal("1.08"),
            "AZN-USD", new BigDecimal("0.59"),
            "AZN-EUR", new BigDecimal("0.54")
    );

    public BigDecimal getExchangeRate(String from, String to) {
        if (from.equalsIgnoreCase(to)) return BigDecimal.ONE;

        String key = from.toUpperCase() + "-" + to.toUpperCase();
        return rates.getOrDefault(key, BigDecimal.ONE);
    }

}
