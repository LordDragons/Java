package com.example.flascash.repositories;

import com.example.flascash.entities.Account;
import com.example.flascash.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccountOrReceiverAccount(Account senderAccount, Account receiverAccount);
}

