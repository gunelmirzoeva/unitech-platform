package com.unitech.transfer.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class AccountDto {
    private UUID id;
    private String owner;
    private Double balance;
}
