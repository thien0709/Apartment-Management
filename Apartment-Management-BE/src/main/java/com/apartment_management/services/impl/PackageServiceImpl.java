/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Locker;
import com.apartment_management.pojo.Package;
import com.apartment_management.repositories.LockerRepository;
import com.apartment_management.repositories.PackageRepository;
import com.apartment_management.repositories.UserRepository;
import com.apartment_management.services.PackageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author thien
 */
@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageRepository packageRepo;
    @Autowired
    private LockerRepository lockerRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Package createPackage(String name, int userId) {
        Locker locker = lockerRepository.getLockerById(userId);
        if (locker == null) {
            var user = userRepository.getUserById(userId);
            lockerRepository.createLocker(user);
        }
        return this.packageRepo.createPackage(name, userId);
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

    @Override
    public Package getPackageById(int packageId) {
        return packageRepo.getPackageById(packageId);
    }

    @Override
    public void updatePackageStatus(int packageId, String status) {
        packageRepo.updatePackageStatus(packageId, status);
    }

    @Override
    public void deletePackage(int packageId) {
        packageRepo.deletePackage(packageId);
    }

}
