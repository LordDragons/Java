package com.example.flascash.service;

import com.example.flascash.entities.Account;
import com.example.flascash.entities.Transaction;
import com.example.flascash.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    // Méthode pour récupérer l'historique des transactions pour un compte
    public List<Transaction> getTransactionHistory(Account account) {
        return transactionRepository.findBySenderAccountOrReceiverAccount(account, account);
    }
}

