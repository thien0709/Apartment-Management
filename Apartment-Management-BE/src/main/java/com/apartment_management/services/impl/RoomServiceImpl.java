/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Room;
import com.apartment_management.repositories.RoomRepository;
import com.apartment_management.services.RoomService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepo;

    @Override
    public Room getRoomById(int roomId) {
        return this.getRoomById(roomId);
    }

    @Override
    public List<Room> getRoomsByFloorId(int floorId) {
        return roomRepo.getRoomByFloorId(floorId);
    }

}
