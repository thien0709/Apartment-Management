/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.services;

import com.apartment_management.pojo.Locker;

/**
 *
 * @author thien
 */
public interface LockerService {
    void creatLocker(int userId);
    Locker getLockerById(int userId);
    void deleteLocker(int userId);
}
