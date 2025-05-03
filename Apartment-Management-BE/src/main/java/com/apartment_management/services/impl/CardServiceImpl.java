/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Card;
import com.apartment_management.repository.CardRepository;
import com.apartment_management.repository.UserRepository;
import com.apartment_management.services.CardService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author thien
 */
@Service
@Transactional
public class CardServiceImpl implements CardService{
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Card createCard(Card card) {
        card.setIssueDate(LocalDate.now());
       return cardRepository.save(card);
    }

    @Override
    public List<Card> getCardsByUser(Integer userId) {
        
    }

    @Override
    public Card updateCard(Long id, Card card) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deactivateCard(Integer cardId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

}
