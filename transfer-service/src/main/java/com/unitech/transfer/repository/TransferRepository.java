package com.unitech.transfer.repository;

import com.unitech.transfer.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    boolean existsByIdempotencyKey(String idempotencyKey);
}
