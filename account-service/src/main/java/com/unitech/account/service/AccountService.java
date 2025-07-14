package com.unitech.account.service;

import com.unitech.account.model.Account;
import com.unitech.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;


    public Account createAccount(Long userId) {
        Account newAccount = new Account();
        newAccount.setUserId(userId);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setActive(true);
        newAccount.setAccountNumber(generateUniqueAccountNumber());

        return accountRepository.save(newAccount);
    }


    public List<Account> getActiveUserAccounts(Long userId) {
        return accountRepository.findByUserIdAndActiveTrue(userId);
    }


    private String generateUniqueAccountNumber() {
        return "AZ" + UUID.randomUUID().toString().replace("-", "").substring(0, 14).toUpperCase();
    }
}