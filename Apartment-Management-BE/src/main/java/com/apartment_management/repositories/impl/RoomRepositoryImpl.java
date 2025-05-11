/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repositories.impl;

import com.apartment_management.pojo.Room;
import com.apartment_management.repositories.RoomRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class RoomRepositoryImpl implements RoomRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    @Override
    public List<Room> getRoomByFloorId(int floorId) {
        // Lấy session từ factory
        Session session = this.factory.getObject().getCurrentSession();
        
        // Sử dụng HQL để truy vấn các phòng theo floorId
        String hql = "FROM Room WHERE floorId.id = :floorId";
        Query<Room> query = session.createQuery(hql, Room.class);
        query.setParameter("floorId", floorId);
        
        // Trả về danh sách các phòng
        return query.getResultList();
    }

    @Override
    public Room getRoomById(int roomId) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Room.class, roomId);
    }
    
}
