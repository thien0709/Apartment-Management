package com.apartment_management.controllers;

import com.apartment_management.pojo.User;
import com.apartment_management.repositories.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author thien
 */

@Controller
@RequestMapping("/api")
public class ApiAdminController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admins")
    public List<User> getAllAdmins() {
        return userRepository.findByRole("admin");
    }

}
