// In transfer-service
package com.unitech.transfer.service.impl;

import com.unitech.transfer.client.AccountClient;
import com.unitech.transfer.client.InternalTransferRequest;
import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.dto.TransferResponse;
import com.unitech.transfer.model.Transfer;
import com.unitech.transfer.repository.TransferRepository;
import com.unitech.transfer.service.TransferService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final AccountClient accountClient;

    public TransferResponse createTransfer(TransferRequest request) {
        log.info("Processing transfer request from {} to {}", request.getFromIban(), request.getToIban());

        // Basic self-transfer check
        if (request.getFromIban().equals(request.getToIban())) {
            throw new IllegalArgumentException("Sender and receiver accounts cannot be the same.");
        }

        // Build the request for the account-service
        InternalTransferRequest internalRequest = InternalTransferRequest.builder()
                .fromIban(request.getFromIban())
                .toIban(request.getToIban())
                .amount(request.getAmount())
                .build();

        try {
            // 1. Call account-service to perform the transfer
            accountClient.performTransfer(internalRequest);

            // 2. If successful, save a "COMPLETED" record
            Transfer transfer = Transfer.builder()
                    .fromIban(request.getFromIban())
                    .toIban(request.getToIban())
                    .amount(request.getAmount())
                    .status("COMPLETED")
                    .build();
            Transfer savedTransfer = transferRepository.save(transfer);
            log.info("Transfer completed and logged with ID: {}", savedTransfer.getId());
            return mapToResponse(savedTransfer);

        } catch (FeignException e) {
            // 3. If account-service returns an error (4xx), it means validation failed.
            log.error("Transfer failed. Reason from account-service: {}", e.contentUTF8(), e);
            Transfer transfer = Transfer.builder()
                    .fromIban(request.getFromIban())
                    .toIban(request.getToIban())
                    .amount(request.getAmount())
                    .status("FAILED")
                    .failureReason(e.contentUTF8()) // Get error message from the response body
                    .build();
            transferRepository.save(transfer);
            // Re-throw a specific exception to be handled by a global exception handler
            throw new RuntimeException("Transfer failed: " + e.contentUTF8());
        }
    }

    private TransferResponse mapToResponse(Transfer transfer) {
        return TransferResponse.builder()
                .transactionId(transfer.getId())
                .fromIban(transfer.getFromIban())
                .toIban(transfer.getToIban())
                .amount(transfer.getAmount())
                .status(transfer.getStatus())
                .timestamp(transfer.getCreatedAt())
                .build();
    }
}