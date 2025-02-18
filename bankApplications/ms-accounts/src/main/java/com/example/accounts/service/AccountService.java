package com.example.accounts.service;

import com.example.accounts.module.Accounts;
import com.example.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Accounts getAccountByCustomerId(int customerId) {
        return accountRepository.findByCustomerId(customerId);
    }
}

