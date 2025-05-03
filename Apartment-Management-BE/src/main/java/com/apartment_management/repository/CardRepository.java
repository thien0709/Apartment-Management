/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repository;

import com.apartment_management.pojo.Card;
import java.util.List;

/**
 *
 * @author thien
 */
public interface CardRepository {
    List<Card> findByUserId(Integer userId);
}
