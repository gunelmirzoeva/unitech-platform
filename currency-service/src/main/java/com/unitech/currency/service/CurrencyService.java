package com.unitech.currency.service;

import com.unitech.currency.client.MockExchangeApiClient;
import com.unitech.currency.model.ExchangeRequest;
import com.unitech.currency.model.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final MockExchangeApiClient mockExchangeApiClient;

    @Cacheable(value = "exchangeRates", key = "#request.from + '-' + #request.to")
    public ExchangeResponse convert(ExchangeRequest request) {

        BigDecimal rate = mockExchangeApiClient.getExchangeRate(request.getFrom(), request.getTo());

        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid exchange rate: " + rate);
        }

        BigDecimal converted = request.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP);

        return new ExchangeResponse(rate, converted, request.getFrom() + "-" + request.getTo());
    }
}
