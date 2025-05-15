/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Floor;
import com.apartment_management.repositories.FloorRepository;
import com.apartment_management.services.FloorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class FloorServiceImpl implements FloorService{
    @Autowired
    private FloorRepository floorRepo;

    @Override
    public List<Floor> getFloors() {
        return floorRepo.getFloors();   
    }
    
}
