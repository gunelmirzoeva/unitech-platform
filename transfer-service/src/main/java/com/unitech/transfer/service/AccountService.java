package com.unitech.transfer.service;

import com.unitech.transfer.dto.AccountDto;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountDto createAccount(AccountDto request);
    AccountDto getAccountById(UUID id);
    List<AccountDto> getAllAccounts();
}