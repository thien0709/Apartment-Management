/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repositories.impl;

import com.apartment_management.pojo.Card;
import com.apartment_management.repositories.CardRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class CardRepositoryImpl implements CardRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Card addCarrd(Card c) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(c);
        return c;
    }
    @Override
    public List<Card> getCardsByUserId(int userId) {
        Session session = factory.getObject().getCurrentSession();
        Query<Card> query = session.createQuery("FROM Card c WHERE c.userId.id = :userId", Card.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    @Override
    public boolean deleteCard(int cardId) {
        Session session = factory.getObject().getCurrentSession();
        Card c = session.get(Card.class, cardId);
        if (c != null) {
            session.delete(c);
            return true;
        }
        return false;
    }
    
}
