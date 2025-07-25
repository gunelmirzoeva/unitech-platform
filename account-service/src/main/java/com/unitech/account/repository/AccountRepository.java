package com.unitech.account.repository;

import com.unitech.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByIban(String iban);
    Optional<Account> findByUserId(UUID userId);

    //List<Account> findByUserIdAndIsActive(UUID userId, boolean isActive);
}