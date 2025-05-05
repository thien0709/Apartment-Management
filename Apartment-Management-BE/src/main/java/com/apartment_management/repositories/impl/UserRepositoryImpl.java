/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repositories.impl;

import com.apartment_management.pojo.User;
import com.apartment_management.repositories.UserRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUserName(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return (User) q.getSingleResult();

    }

    @Override
    public User addUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);

        return u;
    }

    //đăng nhập ở trang admin
    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUserName(username);
        if (u != null && u.getRole().equals("ADMIN")) {
            return this.passwordEncoder.matches(password, u.getPassword());
        } else {
            return false;
        }
    }

    @Override
    public User authenticateForClient(String username, String password) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        User u = (User) q.getSingleResult();

        if (u != null && u.getRole().equals("RESIDENT") && this.passwordEncoder.matches(password, u.getPassword())) {
            return u;
        } else {
            return null; // Nếu đăng nhập thất bại, trả về null
        }
    }

    //dùng cho api editProfile
    @Override
    public User editProfile(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        if (u != null) {
            s.update(u);
        }
        return u;
    }

    @Override
    public User getUserById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createNamedQuery("User.findById", User.class);
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

}
