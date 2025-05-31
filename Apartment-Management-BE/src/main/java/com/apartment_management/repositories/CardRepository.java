/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;

import com.apartment_management.pojo.Card;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface CardRepository {
    Card addCarrd(Card card);
    List<Card> getCardsByUserId(int userId);
    boolean deleteCard(int cardId);
    List<Card> getAllCards();
    
}
