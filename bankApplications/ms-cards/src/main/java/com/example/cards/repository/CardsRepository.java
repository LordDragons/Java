package com.example.cards.repository;

import com.example.cards.model.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardsRepository extends JpaRepository<Cards, Long> {
    List<Cards> findByCustomerId(Long customerId);
}
