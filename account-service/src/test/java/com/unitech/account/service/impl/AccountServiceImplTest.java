package com.unitech.account.service.impl;

import com.unitech.account.dto.AccountResponse;
import com.unitech.account.dto.CreateAccountRequest;
import com.unitech.account.exception.AccountNotFoundException;
import com.unitech.account.exception.InsufficientFundsException;
import com.unitech.account.model.Account;
import com.unitech.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private CreateAccountRequest createAccountRequest;
    private UUID userId;
    private String iban;
    private BigDecimal initialBalance;
    private String currency;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        iban = "AZ12345678901234567890";
        initialBalance = new BigDecimal("1000.00");
        currency = "USD";

        createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setUserId(userId);
        createAccountRequest.setInitialBalance(initialBalance);
        createAccountRequest.setCurrency(currency);

        account = Account.builder()
                .id(UUID.randomUUID())
                .iban(iban)
                .userId(userId)
                .balance(initialBalance)
                .currency(currency)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    @Test
    void createAccount_ValidRequest_ReturnsAccountResponse() {
        // Arrange
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        AccountResponse response = accountService.createAccount(createAccountRequest);

        // Assert
        assertNotNull(response);
        assertEquals(account.getId(), response.getId());
        assertEquals(account.getIban(), response.getIban());
        assertEquals(account.getUserId(), response.getUserId());
        assertEquals(account.getBalance(), response.getBalance());
        assertEquals(account.getCurrency(), response.getCurrency());
        assertTrue(response.isActive());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_NegativeInitialBalance_ThrowsIllegalArgumentException() {
        // Arrange
        createAccountRequest.setInitialBalance(new BigDecimal("-100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(createAccountRequest));
        assertEquals("Initial balance cannot be negative.", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void getAccountByUserId_ExistingUserId_ReturnsAccountResponse() {
        // Arrange
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));

        // Act
        AccountResponse response = accountService.getAccountByUserId(userId);

        // Assert
        assertNotNull(response);
        assertEquals(account.getId(), response.getId());
        assertEquals(account.getIban(), response.getIban());
        assertEquals(account.getUserId(), response.getUserId());
        verify(accountRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getAccountByUserId_NonExistingUserId_ThrowsAccountNotFoundException() {
        // Arrange
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountByUserId(userId));
        assertEquals("Account not found for user: " + userId, exception.getMessage());
        verify(accountRepository, times(1)).findByUserId(userId);
    }



    @Test
    void getAccountByIban_ExistingIban_ReturnsAccountResponse() {
        // Arrange
        when(accountRepository.findByIban(iban)).thenReturn(Optional.of(account));

        // Act
        AccountResponse response = accountService.getAccountByIban(iban);

        // Assert
        assertNotNull(response);
        assertEquals(account.getIban(), response.getIban());
        assertEquals(account.getUserId(), response.getUserId());
        verify(accountRepository, times(1)).findByIban(iban);
    }

    @Test
    void getAccountByIban_NonExistingIban_ThrowsAccountNotFoundException() {
        // Arrange
        when(accountRepository.findByIban(iban)).thenReturn(Optional.empty());

        // Act & Assert
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountByIban(iban));
        assertEquals("Account not found for IBAN: " + iban, exception.getMessage());
        verify(accountRepository, times(1)).findByIban(iban);
    }



    @Test
    void updateBalance_ValidAmount_UpdatesBalanceSuccessfully() {
        // Arrange
        BigDecimal amount = new BigDecimal("500.00");
        when(accountRepository.findByIban(iban)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        accountService.updateBalance(iban, amount);

        // Assert
        verify(accountRepository, times(1)).findByIban(iban);
        verify(accountRepository, times(1)).save(any(Account.class));
        assertEquals(initialBalance.add(amount), account.getBalance());
    }

    @Test
    void updateBalance_InactiveAccount_ThrowsIllegalStateException() {
        // Arrange
        account.setActive(false);
        when(accountRepository.findByIban(iban)).thenReturn(Optional.of(account));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> accountService.updateBalance(iban, new BigDecimal("500.00")));
        assertEquals("Cannot update balance of inactive account.", exception.getMessage());
        verify(accountRepository, times(1)).findByIban(iban);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateBalance_InsufficientFunds_ThrowsInsufficientFundsException() {
        // Arrange
        BigDecimal amount = new BigDecimal("-2000.00"); // More than balance
        when(accountRepository.findByIban(iban)).thenReturn(Optional.of(account));

        // Act & Assert
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class,
                () -> accountService.updateBalance(iban, amount));
        assertEquals("Insufficient balance.", exception.getMessage());
        verify(accountRepository, times(1)).findByIban(iban);
        verify(accountRepository, never()).save(any(Account.class));
    }


    @Test
    void deleteAllAccounts_SuccessfullyDeletesAll() {
        // Act
        accountService.deleteAllAccounts();

        // Assert
        verify(accountRepository, times(1)).deleteAll();
    }

    @Test
    void getAllAccounts_ReturnsListOfAccounts() {
        // Arrange
        Account anotherAccount = Account.builder()
                .id(UUID.randomUUID())
                .iban("AZ98765432109876543210")
                .userId(UUID.randomUUID())
                .balance(new BigDecimal("2000.00"))
                .currency(currency)
                .isActive(true)
                .build();
        when(accountRepository.findAll()).thenReturn(Arrays.asList(account, anotherAccount));

        // Act
        List<AccountResponse> responses = accountService.getAllAccounts();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(account.getIban(), responses.get(0).getIban());
        assertEquals(anotherAccount.getIban(), responses.get(1).getIban());
        verify(accountRepository, times(1)).findAll();
    }
}