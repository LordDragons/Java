package com.example.loans.service;

import com.example.loans.model.Loans;
import com.example.loans.repository.LoansRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoansService {

    private static final Logger logger = LoggerFactory.getLogger(LoansService.class);
    private final LoansRepository loansRepository;

    public LoansService(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    public List<Loans> getLoansByCustomerId(int customerId) {
        logger.info("🔍 Recherche des prêts pour customerId: {}", customerId);
        return loansRepository.findByCustomerIdOrderByStartDtDesc(customerId);
    }
}


