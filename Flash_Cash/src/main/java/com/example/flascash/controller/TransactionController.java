package com.example.flascash.controller;

import com.example.flascash.entities.Account;
import com.example.flascash.entities.Transaction;
import com.example.flascash.entities.User;
import com.example.flascash.service.AccountService;
import com.example.flascash.service.SessionService;
import com.example.flascash.service.TransactionService;
import com.example.flascash.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/transaction-history")
    public String viewTransactionHistory(Model model) {
        // Récupérer l'utilisateur connecté
        User currentUser = sessionService.sessionUser();

        // Récupérer les comptes de l'utilisateur
        List<Account> userAccounts = currentUser.getAccounts();

        // Récupérer toutes les transactions associées à ces comptes
        List<Transaction> transactions = new ArrayList<>();
        for (Account account : userAccounts) {
            transactions.addAll(transactionService.getTransactionHistory(account));
        }

        // Supprimer les doublons en utilisant un Set ou un filtrage
        List<Transaction> allTransactions = transactions.stream()
                .distinct() // Évite les doublons
                .toList();

        // Ajouter les données au modèle
        model.addAttribute("allTransactions", allTransactions);
        model.addAttribute("user", currentUser);

        return "transaction-history";
    }

    @GetMapping(path = "/send/{friendId}")
    public String sendTransaction(@PathVariable Long friendId, Model model) {
        User friend = userService.findById(friendId);
        List<Account> friendAccounts = friend.getAccounts();
        List<Account> userAccounts = sessionService.sessionUser().getAccounts();
        model.addAttribute("friendAccounts", friendAccounts);
        model.addAttribute("userAccounts", userAccounts);
        model.addAttribute("transaction", new Transaction());
        return "send-transaction";

    }

    @PostMapping(path = "/send")
    public String processTransaction(@ModelAttribute Transaction transaction, Model model) {
        transaction.setDate(LocalDateTime.now());

        double fee = transaction.getAmount() * 0.005;
        double totalAmount = transaction.getAmount() + fee;

        if (!accountService.hasSufficientBalance(totalAmount, transaction.getSenderAccount())) {
            model.addAttribute("error", "Insufficient funds to complete the transaction, including the fee.");
            return "send-transaction";
        }

        accountService.removeMoney(totalAmount, transaction.getSenderAccount());
        accountService.addMoney(transaction.getAmount(), transaction.getReceiverAccount());

        transaction.setFee(String.valueOf(fee));
        transactionService.save(transaction);

        return "redirect:/";
    }

    //Pour le transfert entre compte d'une même personne

    @PostMapping("/transfer")
    public String transferBetweenAccounts(
            @RequestParam Long senderAccount,
            @RequestParam Long receiverAccount,
            @RequestParam double amount,
            Model model) {
        try {
            // Récupérer l'utilisateur connecté
            User currentUser = sessionService.sessionUser();
            Account source = accountService.findById(senderAccount);
            Account target = accountService.findById(receiverAccount);

            // Vérifier que les comptes appartiennent à l'utilisateur
            if (!currentUser.getAccounts().contains(source) || !currentUser.getAccounts().contains(target)) {
                model.addAttribute("transferError", "Both accounts must belong to you.");
            } else if (source.getBalance() < amount) {
                model.addAttribute("transferError", "Insufficient funds in the source account.");
            } else {
                // Effectuer le transfert
                accountService.removeMoney(amount, source);
                accountService.addMoney(amount, target);

                // Ajouter la transaction de transfert
                Transaction transaction = new Transaction();
                transaction.setSenderAccount(source);
                transaction.setReceiverAccount(target);
                transaction.setAmount(amount);
                transaction.setDate(LocalDateTime.now());
                transaction.setDescription("Transfer between accounts");
                transactionService.save(transaction);

                model.addAttribute("transferSuccess", "Transfer completed successfully!");
            }
        } catch (Exception e) {
            model.addAttribute("transferError", "An error occurred during the transfer.");
        }

        // Ajouter les comptes mis à jour de l'utilisateur au modèle
        model.addAttribute("accounts", sessionService.sessionUser().getAccounts());

        // Retourner la page des comptes pour afficher le résultat
        return "redirect:/account/show";
    }
}
