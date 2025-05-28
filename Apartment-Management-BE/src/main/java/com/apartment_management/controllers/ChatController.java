/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.User;
import com.apartment_management.services.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author thien
 */
@Controller
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showChatPage(Model model, Principal principal) {
        User u = this.userService.getUserByUserName(principal.getName());
        model.addAttribute("admin", u);
        String firebaseApiKey = env.getProperty("firebase.apiKey");
        model.addAttribute("firebaseApiKey", firebaseApiKey);
        return "chat_admin";
    }
}
