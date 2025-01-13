package com.example.flascash.service;

import com.example.flascash.entities.Account;
import com.example.flascash.entities.User;
import com.example.flascash.repositories.AccountRepository;
import com.example.flascash.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionService sessionService;

    public Account saveAccount(String iban, User user) {
        if (accountRepository.findByIban(iban).isPresent()) {
            throw new IllegalArgumentException("account already exists");
        }

        Account account = new Account(user, iban, 0.0);
        accountRepository.save(account);
        return account;
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).get();
    }

    public void addMoney(double money, Account account) {
        account.setBalance(account.getBalance() + money);
        accountRepository.save(account);
    }

    public void removeMoney(double money, Account account) {
        account.setBalance(account.getBalance() - money);
        accountRepository.save(account);
    }

    // Nouvelle méthode pour vérifier le solde suffisant
    public boolean hasSufficientBalance(double amount, Account account) {
        if (account == null || account.getBalance() == null) {
            return false; // Vérifiez si le compte ou le solde est null.
        }
        return account.getBalance() >= amount;
    }
}
