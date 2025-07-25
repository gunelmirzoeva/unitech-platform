package com.unitech.transfer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service") // Eureka application name
public interface AccountClient {

    @PostMapping("/internal/transfer") // This is the new endpoint we will create
    void performTransfer(@RequestBody InternalTransferRequest request);
}