/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;

import com.apartment_management.pojo.Locker;
import com.apartment_management.pojo.User;

/**
 *
 * @author thien
 */
public interface LockerRepository {
    void createLocker(User u);
    Locker getLockerById(int userId);
    void deleteLocker(int userId);
}
