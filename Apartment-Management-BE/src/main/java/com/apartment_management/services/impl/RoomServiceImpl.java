/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Room;
import com.apartment_management.repositories.RoomRepository;
import com.apartment_management.services.RoomService;
import java.util.Collections;
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
    public List<Room> getRoomsByFloorId(int floorId) {
        if (floorId <= 0) {
            System.out.println("Invalid floorId: " + floorId);
            return Collections.emptyList();
        }
        try {
            List<Room> rooms = roomRepo.getRoomByFloorId(floorId);
            System.out.println("Retrieved " + rooms.size() + " rooms for floorId: " + floorId);
            return rooms;
        } catch (Exception e) {
            System.err.println("Error retrieving rooms for floorId: " + floorId + ", Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Room getRoomById(int roomId) {
        try {
            Room room = roomRepo.getRoomById(roomId);
            System.out.println("Retrieved room for roomId: " + roomId + (room != null ? ", Room: " + room.getRoomNumber() : ", Not found"));
            return room;
        } catch (Exception e) {
            System.err.println("Error retrieving room for roomId: " + roomId + ", Error: " + e.getMessage());
            return null;
        }
    }

}
