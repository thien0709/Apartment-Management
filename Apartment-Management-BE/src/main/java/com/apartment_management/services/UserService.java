/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.services;

import com.apartment_management.pojo.User;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author thien
 */
public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);

    User addUser(User user);

    User authUser(String username, String password);

    void changePassword();

    User addAdmin(User user);

    boolean userExistsByUsername(String username);

    void deleteUser(int id);
}
