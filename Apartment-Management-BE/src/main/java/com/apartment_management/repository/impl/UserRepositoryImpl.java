/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repository.impl;

import com.apartment_management.pojo.User;
import com.apartment_management.repository.UserRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public User getUserByUsername(String username) {
        Query query = getCurrentSession().createQuery("FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);
        return (User) query.getSingleResult();
    }

    @Override
    public User getUserById(Integer id) {
        return getCurrentSession().get(User.class, id);
    }

    @Override
    public User addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        getCurrentSession().save(user);
        return user;
    }

    @Override
    public User authUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public boolean userExistsByUsername(String username) {
        try {
            getUserByUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void changePassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        getCurrentSession().update(user);
    }

    @Override
    public void deleteUser(int id) {
        User user = getUserById(id);
        if (user != null) {
            getCurrentSession().delete(user);
        }
    }

}
