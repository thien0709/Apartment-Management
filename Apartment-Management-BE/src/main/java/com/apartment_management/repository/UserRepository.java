/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repository;

import com.apartment_management.pojo.User;

/**
 *
 * @author thien
 */
public interface UserRepository {

    User getUserByUsername(String username);

    User getUserById(Integer id);

    User addUser(User user);

    User authUser(String username, String password);

    boolean userExistsByUsername(String username);

    void changePassword(User user);

    void deleteUser(int id);
}
