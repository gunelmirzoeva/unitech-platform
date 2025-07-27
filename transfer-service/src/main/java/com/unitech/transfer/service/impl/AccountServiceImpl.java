package com.unitech.transfer.service.impl;

import com.unitech.transfer.dto.AccountDto;
import com.unitech.transfer.exception.DuplicateAccountException;
import com.unitech.transfer.exception.InvalidAccountException;
import com.unitech.transfer.model.Account;
import com.unitech.transfer.repository.AccountRepository;
import com.unitech.transfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountDto createAccount(AccountDto request) {
        log.info("Creating account with IBAN: {}", request.getIban());

        // Check if IBAN already exists
        if (accountRepository.findByIban(request.getIban()).isPresent()) {
            throw new DuplicateAccountException("Account with IBAN " + request.getIban() + " already exists");
        }

        // Create and save account
        Account account = Account.builder()
                .iban(request.getIban())
                .balance(request.getBalance())
                .build();

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getId());

        return mapToDto(savedAccount);
    }

    public AccountDto getAccountById(UUID id) {
        log.info("Retrieving account with ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new InvalidAccountException("Account not found with ID: " + id));
        return mapToDto(account);
    }

    public List<AccountDto> getAllAccounts() {
        log.info("Retrieving all accounts");
        return accountRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AccountDto mapToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setIban(account.getIban());
        dto.setBalance(account.getBalance());
        return dto;
    }
}