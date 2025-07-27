package com.unitech.transfer.controller;

import com.unitech.transfer.dto.TransferRequest;
import com.unitech.transfer.dto.TransferResponse;
import com.unitech.transfer.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transfer", description = "Transfer management API")
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @Operation(summary = "Create a new transfer", description = "Initiates a money transfer between two accounts")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transfer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or insufficient funds"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.createTransfer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}