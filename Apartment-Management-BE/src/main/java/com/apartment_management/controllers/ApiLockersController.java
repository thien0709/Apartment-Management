/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.Package;
import com.apartment_management.services.LockerService;
import com.apartment_management.services.PackageService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author thien
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiLockersController {

    @Autowired
    private LockerService lockerService;

    @Autowired
    private PackageService packageService;

    @PostMapping("/locker")
    @ResponseStatus(HttpStatus.OK)
    public void creatLocker(@RequestBody Map<String, Object> body) {
        Object userIdObj = body.get("userId");
        if (userIdObj instanceof Number number) {
            int userId = number.intValue(); 
            this.lockerService.creatLocker(userId);
        } else {
            throw new IllegalArgumentException("Invalid or missing 'userId' in request body");
        }
    }

    ;
    
    @GetMapping("/locker/{userId}")
    public List<Package> findByUserId(@PathVariable(value = "userId") int userId) {
        return this.packageService.findByUserId(userId);
    }

    ;
    
    @GetMapping("/locker/{userId}/{status}")
    public List<Package> findByUserIdAndStatus(@PathVariable(value = "userId") int userId,
            @PathVariable(value = "status") String status) {
        return this.packageService.findByUserIdAndStatus(userId, status);
    }

}
