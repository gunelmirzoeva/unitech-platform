package com.unitech.account.service.impl;

import com.unitech.account.dto.AccountResponse;
import com.unitech.account.dto.CreateAccountRequest;
import com.unitech.account.exception.AccountNotFoundException;
import com.unitech.account.exception.InsufficientFundsException;
import com.unitech.account.model.Account;
import com.unitech.account.repository.AccountRepository;
import com.unitech.account.service.AccountService;
import com.unitech.account.utils.IBANGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account for user: {}", request.getUserId());
        if(request.getInitialBalance().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        String generatedIban = IBANGenerator.generate();
        Account account = Account.builder()
                .iban(generatedIban)
                .userId(request.getUserId())
                .balance(request.getInitialBalance())
                .currency(request.getCurrency())
                .isActive(true)
                .build();

        Account saved = accountRepository.save(account);
        log.info("Account created with IBAN: {}", generatedIban);
        return mapToResponse(saved);

    }

    @Override
    public AccountResponse getAccountByUserId(UUID userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for user: " + userId));
        return mapToResponse(account);
    }

    @Override
    public AccountResponse getAccountByIban(String iban) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for IBAN: " + iban));
        return mapToResponse(account);
    }

    @Override
    public void updateBalance(String iban, BigDecimal amount) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for IBAN: " + iban));

        if (!account.isActive()) {
            throw new IllegalStateException("Cannot update balance of inactive account.");
        }

        BigDecimal newBalance = account.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient balance.");
        }

        account.setBalance(newBalance);
        accountRepository.save(account);
        log.info("Balance updated for IBAN: {} (New balance: {})", iban, newBalance);
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .userId(account.getUserId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .isActive(account.isActive())
                .build();
    }
}
