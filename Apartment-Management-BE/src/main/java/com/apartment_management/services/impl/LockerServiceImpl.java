/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Locker;
import com.apartment_management.pojo.User;
import com.apartment_management.repositories.LockerRepository;
import com.apartment_management.repositories.UserRepository;
import com.apartment_management.services.LockerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author thien
 */
@Service
public class LockerServiceImpl implements LockerService{
    @Autowired
    private LockerRepository lockerRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Override
    public void creatLocker(int userId) {
        User u = this.userRepo.getUserById(userId);
        this.lockerRepo.createLocker(u);
    }

    @Override
    public Locker getLockerById(int userId) {
        return this.lockerRepo.getLockerById(userId);
    }

    @Override
    public void deleteLocker(int userId) {
        this.lockerRepo.deleteLocker(userId);
    }
    @Override
    public List<Locker> findAllLockers() {
        return lockerRepo.findAllLockers();
    }
}
