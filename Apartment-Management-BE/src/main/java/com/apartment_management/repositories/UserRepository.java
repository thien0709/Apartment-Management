/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;

import com.apartment_management.pojo.User;
import org.springframework.stereotype.Repository;


/**
 *
 * @author ADMIN
 */
@Repository
public interface UserRepository {
    User getUserByUserName(String username);
    User getUserById(int id);
    User addUser(User u);
    boolean authenticate(String username, String password);
    User authenticateForClient(String username, String password);
    User editProfile(User user);
}
