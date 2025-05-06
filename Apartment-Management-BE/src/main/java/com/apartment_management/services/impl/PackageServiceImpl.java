/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Package;
import com.apartment_management.repository.PackageRepository;
import com.apartment_management.services.PackageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author thien
 */
@Service
public class PackageServiceImpl implements PackageService{
    
    @Autowired
    private PackageRepository packageRepo;
    
    @Override
    public void createPackage(String name, int userId) {
        this.packageRepo.createPackage(name, userId);
     }

    @Override
    public List<Package> findByUserId(int userId) {
       return this.packageRepo.findByUserId(userId);
    }

    @Override
    public List<Package> findByUserIdAndStatus(int userId, String status) {
        return this.packageRepo.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Package> findByStatus(String status) {
        return this.packageRepo.findByStatus(status);
    }

}
