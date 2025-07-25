package com.unitech.account.service;

import com.unitech.account.dto.AccountResponse;
import com.unitech.account.dto.CreateAccountRequest;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);
    AccountResponse getAccountByUserId(UUID userId);
    AccountResponse getAccountByIban(String iban);
    void updateBalance(String iban, BigDecimal amount);
    void performTransfer(String fromIban, String toIban, BigDecimal amount);
}
