/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.User;
import com.apartment_management.services.UserService;
import com.apartment_management.utils.JwtUtils;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author thien
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    UserService userService;

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = this.userService.getUserByUsername(username);
        if (user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // API tạo user mới (register)
    @PostMapping(path = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addUser(
            @RequestParam Map<String, String> params,
            @RequestParam("avatar") MultipartFile avatar
    ) {
        User u = this.userService.addUser(params, avatar);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }

    @PutMapping("/users/{username}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable String username,
            @RequestBody Map<String, String> payload
    ) {
        String oldPass = payload.get("oldPassword");
        String newPass = payload.get("newPassword");

        try {
            this.userService.changePassword(username, oldPass, newPass);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Incorrect old password");
        }
    }

    // API login trả về JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        try {
            User user = this.userService.getUserByUsername(username);
            if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                String token = JwtUtils.generateToken(username);
                return ResponseEntity.ok(Map.of("token", token));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
    
}