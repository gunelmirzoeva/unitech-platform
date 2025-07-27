package com.unitech.transfer.service.impl;

import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.dto.TransferResponse;
import com.unitech.transfer.exception.InsufficientFundsException;
import com.unitech.transfer.exception.InvalidAccountException;
import com.unitech.transfer.exception.InvalidTransferException;
import com.unitech.transfer.model.Account;
import com.unitech.transfer.model.Transfer;
import com.unitech.transfer.repository.AccountRepository;
import com.unitech.transfer.repository.TransferRepository;
import com.unitech.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Transactional
    public TransferResponse createTransfer(TransferRequest request) {
        log.info("Processing transfer request from {} to {} for amount {}",
                request.getFromIban(), request.getToIban(), request.getAmount());

        // Validate transfer
        if (request.getFromIban().equals(request.getToIban())) {
            throw new InvalidTransferException("Sender and receiver accounts cannot be the same");
        }

        // Find accounts
        Account fromAccount = accountRepository.findByIban(request.getFromIban())
                .orElseThrow(() -> new InvalidAccountException("Source account not found: " + request.getFromIban()));
        Account toAccount = accountRepository.findByIban(request.getToIban())
                .orElseThrow(() -> new InvalidAccountException("Destination account not found: " + request.getToIban()));

        // Check balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + request.getFromIban());
        }

        // Update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        // Save updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create and save transfer record
        Transfer transfer = Transfer.builder()
                .fromIban(request.getFromIban())
                .toIban(request.getToIban())
                .amount(request.getAmount())
                .status("SUCCESS")
                .build();

        Transfer savedTransfer = transferRepository.save(transfer);
        log.info("Transfer completed successfully with ID: {}", savedTransfer.getId());

        return TransferResponse.builder()
                .transactionId(savedTransfer.getId())
                .fromIban(savedTransfer.getFromIban())
                .toIban(savedTransfer.getToIban())
                .amount(savedTransfer.getAmount())
                .status(savedTransfer.getStatus())
                .timestamp(savedTransfer.getCreatedAt())
                .build();
    }
}