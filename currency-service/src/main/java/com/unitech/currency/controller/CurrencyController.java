package com.unitech.currency.controller;


import com.unitech.currency.model.ExchangeRequest;
import com.unitech.currency.model.ExchangeResponse;
import com.unitech.currency.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("/convert")
    public ResponseEntity<ExchangeResponse> convert(@Valid @RequestBody ExchangeRequest request) {
        ExchangeResponse response = currencyService.convert(request);
        return ResponseEntity.ok(response);
    }
}
