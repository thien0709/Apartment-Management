/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repository.impl;

import com.apartment_management.pojo.Locker;
import com.apartment_management.pojo.User;
import com.apartment_management.repository.LockerRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author thien
 */
@Repository
@Transactional
public class LockerRepositoryImpl implements LockerRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }
    
    @Override
    public void createLocker(User u) {
        Locker locker = new Locker();
        locker.setId(u.getId());
//        locker.setUser(u);
        getCurrentSession().persist(locker);
    }



    @Override
    public Locker getLockerById(int userId) {
        return getCurrentSession().get(Locker.class, userId);
    }

    @Override
    public void deleteLocker(int userId) {
        Locker locker = getLockerById(userId);
        if (locker != null) {
            this.getCurrentSession().remove(locker);
        }
    }

}
