/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;
import com.apartment_management.pojo.Package;
import java.util.List;

/**
 *
 * @author thien
 */
public interface PackageRepository {
    void createPackage(String name, int userId);
    List<Package> findByUserId(int userId);
    List<Package> findByUserIdAndStatus(int userId,String status);
    List<Package> findByStatus(String status);
    void updatePackageStatus(int packageId, String status);
    void deletePackage(int packageId);
    Package getPackageById(int packageId);
}
