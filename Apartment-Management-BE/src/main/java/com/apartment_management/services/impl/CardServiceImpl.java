/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Card;

import com.apartment_management.repositories.CardRepository;
import com.apartment_management.repositories.UserRepository;
import com.apartment_management.services.CardService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardRepository cardRepo;

    @Override
    public Card addCard(Card card) {
        if (userRepo.getUserById(card.getUserId().getId()) != null) {
            card.setStatus("active");
            card.setIssueDate(new Date());
            Calendar cal = Calendar.getInstance();
            cal.setTime(card.getIssueDate());
            cal.add(Calendar.MONTH, 3);
            card.setExpirationDate(cal.getTime());
            return cardRepo.addCarrd(card);
        }
        return null;
    }

    @Override
    public List<Card> getCardsByUserId(int userId) {
        if(userRepo.getUserById(userId) != null) {
            return cardRepo.getCardsByUserId(userId);
        }
        return null;
    }
    @Override
    public boolean deleteCard(int cardId) {
        return cardRepo.deleteCard(cardId);
    }
    @Override
    public List<Card> getAllCards() {
        return cardRepo.getAllCards();
    }

}
