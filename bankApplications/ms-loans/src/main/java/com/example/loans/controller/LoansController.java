package com.example.loans.controller;

import com.example.loans.model.Loans;

import com.example.loans.request.CustomerRequest;
import com.example.loans.service.LoansService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/myLoans")
public class LoansController {

    private final LoansService loansService;

    public LoansController(LoansService loansService) {
        this.loansService = loansService;
    }

    @PostMapping
    public ResponseEntity<List<Loans>> getLoansByCustomerId(@RequestBody CustomerRequest request) {
        if (request == null || request.getCustomerId() == 0) {
            return ResponseEntity.badRequest().build();
        }
        List<Loans> loans = loansService.getLoansByCustomerId(request.getCustomerId());
        if (loans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(loans);
    }
}



