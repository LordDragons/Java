package com.example.flascash.controller;

import com.example.flascash.entities.Account;
import com.example.flascash.entities.User;
import com.example.flascash.service.AccountService;
import com.example.flascash.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
@RequestMapping(path = "/account")
public class AccountController {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/show")
    public String showAccountsAdd(Model model) {
        User user = sessionService.sessionUser();
        model.addAttribute("accounts", user.getAccounts());
        return "account/accounts";
    }

    @PostMapping(path = "/add")
    public String addAccount(@RequestParam String iban, Model model) {
        try {
            User user = sessionService.sessionUser();
            accountService.saveAccount(iban, user);
            model.addAttribute("accounts", user.getAccounts());
            model.addAttribute("success", true);
            return "account/accounts";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            User user = sessionService.sessionUser();
            model.addAttribute("accounts", user.getAccounts());
            return "account/accounts";
        }
    }


    @GetMapping(path = "/show-add/balance/{id}")
    public String showBalanceAdd(@PathVariable("id") Long id,Model model) {
        User user = sessionService.sessionUser();
        Account account = accountService.findById(id);

        if (!account.getUser().getId().equals(user.getId())) {
            return "redirect:/";
        }

        model.addAttribute("account", account);
        return "account/add-balance";
    }

    @PostMapping(path = "/add/balance/{id}")
    public String addBalance(@PathVariable("id") Long id, @RequestParam Double amount, Model model) {
        try {
            User user = sessionService.sessionUser();
            Account account = accountService.findById(id);

            if (!account.getUser().getId().equals(user.getId())) {
                throw new SecurityException("Unauthorized access to account");
            }

            accountService.addMoney(amount, account);
            model.addAttribute("success", true);
            return "redirect:/account/show";
        } catch (Exception e) {
            Account account = accountService.findById(id);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("success", false);
            return "redirect:/account/show-add/balance/{" + account.getId() + "}";
            
        }
    }
}
