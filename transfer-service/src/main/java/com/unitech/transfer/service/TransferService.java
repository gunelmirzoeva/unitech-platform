package com.unitech.transfer.service;

import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.enums.TransferStatus;
import com.unitech.transfer.event.TransferEventPublisher;
import com.unitech.transfer.exception.CurrencyMismatchException;
import com.unitech.transfer.exception.DuplicateTransferException;
import com.unitech.transfer.exception.InsufficientFundsException;
import com.unitech.transfer.exception.InvalidTransferException;
import com.unitech.transfer.model.Transfer;
import com.unitech.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferService {

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransferEventPublisher eventPublisher;

    @Transactional
    public Transfer transfer(TransferRequest request) {
        boolean balanceChanged = false;

        try {

            AccountResponse sender = accountClient.getAccountById(request.getSenderAccountId());
            AccountResponse receiver = accountClient.getAccountById(request.getReceiverAccountId());


            validateTransfer(sender, receiver, request);


            accountClient.decreaseBalance(sender.getId(), request.getAmount());
            balanceChanged = true;


            accountClient.increaseBalance(receiver.getId(), request.getAmount());


            Transfer transfer = createTransferEntity(request, TransferStatus.SUCCESS);

            Transfer saved = transferRepository.save(transfer);
            eventPublisher.publishTransferEvent(saved);
            return saved;

        } catch (Exception e) {

            if (balanceChanged) {
                compensateTransfer(request);
            }


            Transfer failedTransfer = createTransferEntity(request, TransferStatus.FAILED);
            transferRepository.save(failedTransfer);

            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }
    }

    private void compensateTransfer(TransferRequest request) {
        accountClient.increaseBalance(request.getSenderAccountId(), request.getAmount());
        accountClient.decreaseBalance(request.getReceiverAccountId(), request.getAmount());
    }

    private void validateTransfer(AccountResponse sender, AccountResponse receiver,
                                  TransferRequest request) {
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient balance");
        }
        if (!sender.getCurrency().equals(receiver.getCurrency())) {
            throw new CurrencyMismatchException("Currency mismatch");
        }
        if (transferRepository.existsByIdempotencyKey(request.getIdempotencyKey())) {
            throw new DuplicateTransferException("Duplicate transfer request");
        }
        if (request.getSenderAccountId().equals(request.getReceiverAccountId())) {
            throw new InvalidTransferException("Cannot transfer to same account");
        }
    }

    private Transfer createTransferEntity(TransferRequest request, TransferStatus status) {
        return Transfer.builder()
                .senderAccountId(request.getSenderAccountId())
                .receiverAccountId(request.getReceiverAccountId())
                .amount(request.getAmount())
                .status(status.name())
                .idempotencyKey(request.getIdempotencyKey())
                .timestamp(LocalDateTime.now())
                .build();
    }
}