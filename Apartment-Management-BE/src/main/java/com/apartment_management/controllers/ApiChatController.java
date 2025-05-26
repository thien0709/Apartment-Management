package com.apartment_management.controllers;

import com.apartment_management.pojo.User;
import com.apartment_management.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author thien
 */

@RestController
@RequestMapping("/api")
public class ApiChatController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/admins")
    public List<User> getAllAdmins() {
        return userService.findByRole("ADMIN");
    }

}
