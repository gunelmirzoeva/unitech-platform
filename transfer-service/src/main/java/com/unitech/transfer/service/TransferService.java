package com.unitech.transfer.service;

import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.dto.TransferResponse;

public interface TransferService {
    TransferResponse createTransfer(TransferRequest request);
}
