/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.User;
import com.apartment_management.services.UserService;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
public class ApiUserControllers {

    @Autowired
    private UserService userService;

    @PutMapping(path = "/users/{id}", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<User> editProfile(
            @PathVariable("id") int id,
            @ModelAttribute User user) {

        User updatedUser = userService.editProfile(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        User authenticatedUser = userService.authenticateForClient(username, password);
        if (authenticatedUser != null) {
            return ResponseEntity.ok(authenticatedUser); // Trả về user nếu đăng nhập thành công
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về lỗi nếu đăng nhập thất bại
        }
    }

}
