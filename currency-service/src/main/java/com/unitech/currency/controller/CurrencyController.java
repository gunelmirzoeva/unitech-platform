package com.unitech.currency.controller;

import com.unitech.currency.dtos.ConversionRequest;
import com.unitech.currency.dtos.ConversionResponse;
import com.unitech.currency.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Convert an amount from one currency to another", description = "Converts a specified amount from source currency to target currency using real-time exchange rates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful conversion"),
            @ApiResponse(responseCode = "500", description = "Internal server error or API failure")
    })
    @GetMapping("/convert")
    public ConversionResponse convert(
            @Parameter(description = "Source currency code (e.g., USD)", example = "USD") @RequestParam String from,
            @Parameter(description = "Target currency code (e.g., EUR)", example = "EUR") @RequestParam String to,
            @Parameter(description = "Amount to convert", example = "100.0") @RequestParam double amount) {
        ConversionRequest request = new ConversionRequest();
        request.setFromCurrency(from);
        request.setToCurrency(to);
        request.setAmount(amount);
        return currencyService.convertCurrency(request);
    }

}