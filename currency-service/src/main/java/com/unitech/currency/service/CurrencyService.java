package com.unitech.currency.service;

import com.unitech.currency.dtos.ConversionRequest;
import com.unitech.currency.dtos.ConversionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    public CurrencyService(RestTemplate restTemplate,
                           @Value("${exchangerate.api.key}") String apiKey,
                           @Value("${exchangerate.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public ConversionResponse convertCurrency(ConversionRequest request) {
        String url = String.format("%s/%s/pair/%s/%s/%s",
                baseUrl, apiKey, request.getFromCurrency(), request.getToCurrency(), request.getAmount());

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !"success".equals(response.get("result"))) {
            throw new RuntimeException("Failed to fetch conversion rate: " + response.get("error-type"));
        }

        ConversionResponse conversionResponse = new ConversionResponse();
        conversionResponse.setFromCurrency(request.getFromCurrency());
        conversionResponse.setToCurrency(request.getToCurrency());
        conversionResponse.setAmount(request.getAmount());
        conversionResponse.setExchangeRate((Double) response.get("conversion_rate"));
        conversionResponse.setConvertedAmount((Double) response.get("conversion_result"));

        return conversionResponse;
    }

}