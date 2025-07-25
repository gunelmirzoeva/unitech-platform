package com.unitech.transfer.controller;

import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.dto.TransferResponse;
import com.unitech.transfer.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.createTransfer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}