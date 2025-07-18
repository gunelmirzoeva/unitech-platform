package com.unitech.transfer.service;

import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.event.TransferEventPublisher;
import com.unitech.transfer.model.Transfer;
import com.unitech.transfer.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransferEventPublisher eventPublisher;

    public Transfer transfer(TransferRequest request){    }
}
