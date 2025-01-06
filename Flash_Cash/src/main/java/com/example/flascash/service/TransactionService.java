package com.example.flascash.service;

import com.example.flascash.entities.Transaction;
import com.example.flascash.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
