/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repository.impl;

import com.apartment_management.pojo.User;
import com.apartment_management.repository.UserRepository;
import jakarta.data.repository.Repository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 *
 * @author thien
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        Session session = factory.getObject().getCurrentSession();
        try {
            Query q = session.createNamedQuery("User.findByUsername", User.class);
            q.setParameter("username", username);
            return (User) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User addUser(User u) {
        Session session = factory.getObject().getCurrentSession();
        // Encode password trước khi lưu
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        session.persist(u);
        return u;
    }

    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUsername(username);
        if (u != null) {
            return passwordEncoder.matches(password, u.getPassword());
        }
        return false;
    }
}
