package com.example.flascash.entities;

import com.example.flascash.validator.ValidIban;
import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

@Entity
@Validated
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ValidIban
    @Column(unique = true, nullable = false, length = 14)
    private String iban;

    @Column(nullable = false)
    private Double balance;

    public Account() {}

    public Account(User user, String iban, Double balance) {
        this.user = user;
        this.iban = iban;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
