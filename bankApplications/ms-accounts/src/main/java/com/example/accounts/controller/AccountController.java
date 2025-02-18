package com.example.accounts.controller;

import com.example.accounts.module.Accounts;
import com.example.accounts.module.Customer;
import com.example.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/myAccount")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<Accounts> getAccountByCustomerId(@RequestBody Customer customer) {
        int customerId = customer.getCustomerId();
        Accounts account = accountRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(account);
    }
}



