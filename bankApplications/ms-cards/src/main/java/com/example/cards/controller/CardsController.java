package com.example.cards.controller;

import com.example.cards.model.Cards;
import com.example.cards.model.Customer;
import com.example.cards.repository.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/myCards")
public class CardsController {

    @Autowired
    private CardsRepository cardsRepository;

    @PostMapping
    public ResponseEntity<List<Cards>> getCardsByCustomerId(@RequestBody Map<String, Long> requestBody) {
        Long customerId = requestBody.get("customerId");
        if (customerId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Cards> cards = cardsRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(cards);
    }
}