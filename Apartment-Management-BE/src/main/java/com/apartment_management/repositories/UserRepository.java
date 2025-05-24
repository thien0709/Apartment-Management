/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;

import com.apartment_management.pojo.User;
import java.util.List;
import java.util.Map;


/**
 *
 * @author ADMIN
 */
public interface UserRepository {
    User getUserByUserName(String username);
    User getUserById(int id);
    User addUser(User u);
    boolean authenticate(String username, String password);
    User authenticateForClient(String username, String password);
    User editProfile(User user);
    List<User> getUsers(Map<String, String> params);
    boolean deleteUser(int id);
    List<User> findByRole(String role);
    List<Object[]> getUserStatsByPeriod(String period, int year);
}
