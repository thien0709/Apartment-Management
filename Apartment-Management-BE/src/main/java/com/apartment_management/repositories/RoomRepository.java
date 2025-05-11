/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;

import com.apartment_management.pojo.Room;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface RoomRepository {
    List<Room> getRoomByFloorId(int floorId);
    Room getRoomById(int roomId);
    
}
