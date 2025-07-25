package com.unitech.account.controller;

import com.unitech.account.dto.AccountResponse;
import com.unitech.account.dto.CreateAccountRequest;
import com.unitech.account.dto.InternalTransferRequest;
import com.unitech.account.dto.UpdateBalanceRequest;
import com.unitech.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing user accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Create a new account", description = "Creates a new account for a user with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get account by user ID", description = "Retrieves account details for a specific user ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountResponse> getAccountByUserId(@PathVariable UUID userId) {
        AccountResponse response = accountService.getAccountByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/iban/{iban}")
    @Operation(summary = "Get account by IBAN", description = "Retrieves account details for a specific IBAN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountResponse> getAccountByIban(@PathVariable String iban) {
        AccountResponse response = accountService.getAccountByIban(iban);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/iban/{iban}/balance")
    @Operation(summary = "Update account balance", description = "Updates the balance of an account. Positive amount for deposits, negative for withdrawals.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid amount or inactive account"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> updateBalance(@PathVariable String iban, @Valid @RequestBody UpdateBalanceRequest request) {
        accountService.updateBalance(iban, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/internal/transfer")
    @Operation(summary = "Perform internal transfer", description = "Transfers funds between two accounts within the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transfer request or insufficient funds"),
            @ApiResponse(responseCode = "404", description = "One or both accounts not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> performInternalTransfer(@Valid @RequestBody InternalTransferRequest request) {
        accountService.performTransfer(request.getFromIban(), request.getToIban(), request.getAmount());
        return ResponseEntity.ok().build();
    }
    //just for testing and should be deleted
    @DeleteMapping
    @Operation(summary = "Delete all accounts (TEST ONLY)", description = "Deletes all accounts in the system. Use only in a testing environment.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "All accounts deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAllAccounts() {
        accountService.deleteAllAccounts();
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieves a list of all accounts in the system. Use for testing purposes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}