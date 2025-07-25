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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    @Transactional // Ensures atomicity: all or nothing
    public void performTransfer(String fromIban, String toIban, BigDecimal amount) {
        log.info("Initiating transfer from {} to {} for amount {}", fromIban, toIban, amount);

        // 1. Get both accounts and lock them for the transaction
        Account fromAccount = accountRepository.findByIban(fromIban)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found: " + fromIban));

        Account toAccount = accountRepository.findByIban(toIban)
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found: " + toIban));

        // 2. Validate accounts
        if (!fromAccount.isActive() || !toAccount.isActive()) {
            throw new IllegalStateException("One or both accounts are inactive.");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + fromIban);
        }

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            // For simplicity, we assume same currency. Add exchange logic later if needed.
            throw new IllegalArgumentException("Currency mismatch between accounts.");
        }

        // 3. Perform debit and credit
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        // 4. Save both accounts. The transaction will commit here.
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Transfer completed successfully.");
    }

    //this one is for testing delete after your work is done.
    @Override
    @Transactional
    public void deleteAllAccounts() {
        log.info("Deleting all accounts for testing purposes");
        accountRepository.deleteAll();
        log.info("All accounts deleted successfully");
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        log.info("Retrieving all accounts");
        List<Account> accounts = accountRepository.findAll();
        List<AccountResponse> responses = accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        log.info("Retrieved {} accounts", responses.size());
        return responses;
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
