package com.unitech.account.controller;

import com.unitech.account.dto.AccountResponse;
import com.unitech.account.dto.CreateAccountRequest;
import com.unitech.account.dto.UpdateBalanceRequest; // We'll create this DTO
import com.unitech.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/") // Mapped to root because the gateway rewrites the path
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Creates a new account for a user.
     * Endpoint: POST /
     * Note: The gateway routes requests from /accounts to here.
     */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves an account by the user's ID.
     * Endpoint: GET /user/{userId}
     * Note: The gateway routes requests from /accounts/user/{userId} to here.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<AccountResponse> getAccountByUserId(@PathVariable UUID userId) {
        AccountResponse response = accountService.getAccountByUserId(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves an account by its IBAN.
     * Endpoint: GET /iban/{iban}
     * Note: The gateway routes requests from /accounts/iban/{iban} to here.
     */
    @GetMapping("/iban/{iban}")
    public ResponseEntity<AccountResponse> getAccountByIban(@PathVariable String iban) {
        AccountResponse response = accountService.getAccountByIban(iban);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the balance of an account. Can be used for deposits or withdrawals.
     * Endpoint: PATCH /iban/{iban}/balance
     * Note: The gateway routes requests from /accounts/iban/{iban}/balance to here.
     */
    @PatchMapping("/iban/{iban}/balance")
    public ResponseEntity<Void> updateBalance(@PathVariable String iban, @Valid @RequestBody UpdateBalanceRequest request) {
        // The amount can be positive (deposit) or negative (withdrawal)
        accountService.updateBalance(iban, request.getAmount());
        return ResponseEntity.ok().build();
    }
}