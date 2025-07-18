package com.unitech.transfer.service;

import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.event.TransferEventPublisher;
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
    public Transfer transfer(TransferRequest request){
        var sender = accountClient.getAccountById(request.getSenderAccountId());
        var receiver = accountClient.getAccountById(request.getReceiverAccountId());

        if (sender.getBalance().compareTo(request.getAmount()) < 0){
            throw new IllegalArgumentException("Sender does not have enough balance");
        }

        accountClient.decreaseBalance(sender.getId(), request.getAmount());
        accountClient.increaseBalance(receiver.getId(), request.getAmount());

        Transfer transfer = Transfer.builder().senderAccountId(request.getSenderAccountId()).recieverAcoountId(request.getReceiverAccountId()).amount(request.getAmount()).status("Success").timestamp(LocalDateTime.now()).build();

        Transfer saved = transferRepository.save(transfer);
        eventPublisher.publishTransferEvent(saved);
        return saved;
    }
}
