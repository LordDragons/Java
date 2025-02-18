package com.example.accounts.repository;

import com.example.accounts.module.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {
    Accounts findByCustomerId(int customerId);
}
