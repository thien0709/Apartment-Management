/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.Card;
import com.apartment_management.pojo.User;
import com.apartment_management.services.CardService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
public class ApiCardControllers {

    @Autowired
    private CardService cardService;

    @PostMapping("/card/create")
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        if (card.getUserId() == null || card.getUserId().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Card c = cardService.addCard(card);
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(c);
    }

    @GetMapping("/card/user/{userId}")
    public ResponseEntity<List<Card>> getCardsByUser(@PathVariable("userId") int userId) {
        List<Card> cards = cardService.getCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }

    @DeleteMapping("/card/delete/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable("id") int id) {
        boolean deleted = cardService.deleteCard(id);
        if (deleted) {
            return ResponseEntity.ok().body("Card deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
        }
    }

}
