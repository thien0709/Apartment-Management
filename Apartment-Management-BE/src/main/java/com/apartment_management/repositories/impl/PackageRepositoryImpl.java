/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repositories.impl;

import com.apartment_management.pojo.Locker;
import com.apartment_management.pojo.Package;
import com.apartment_management.repositories.PackageRepository;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author thien
 */
@Repository
@Transactional
public class PackageRepositoryImpl implements PackageRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void createPackage(String name, int userId) {
        Locker locker = this.getCurrentSession().find(Locker.class, userId);
        if (locker != null) {
            Package p = new Package();
            p.setName(name);
            p.setCreatedAt(new Date());
            p.setLockerId(locker);
            p.setStatus("Chờ nhận");
            this.getCurrentSession().persist(p);
        }
    }

    @Override
    public List<Package> findByUserId(int userId) {
        Query q = this.getCurrentSession().createQuery("FROM Package p WHERE p.lockerId.id = :userId", Package.class);
        q.setParameter("userId", userId);
        return q.getResultList();
    }

    @Override
    public List<Package> findByUserIdAndStatus(int userId, String status) {
        Query q = this.getCurrentSession().createQuery(
                "FROM Package p WHERE p.lockerId.id = :userId AND p.status = :status", Package.class);
        q.setParameter("userId", userId);
        q.setParameter("status", status);
        return q.getResultList();
    }

    @Override
    public List<Package> findByStatus(String status) {
        Query q = this.getCurrentSession().createQuery(
                "FROM Package p WHERE p.status = :status", Package.class);
        q.setParameter("status", status);
        return q.getResultList();
    }

}
