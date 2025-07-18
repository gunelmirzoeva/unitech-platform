package com.unitech.transfer.controller;

import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.model.Transfer;
import com.unitech.transfer.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    public ResponseEntity<Transfer> makeTransfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferService.transfer(request));
    }
}
