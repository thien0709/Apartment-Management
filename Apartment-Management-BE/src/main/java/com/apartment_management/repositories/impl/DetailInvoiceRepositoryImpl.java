/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repositories.impl;

import com.apartment_management.pojo.DetailInvoice;
import com.apartment_management.repositories.DetailInvoiceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.hibernate.Session;
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
public class DetailInvoiceRepositoryImpl implements DetailInvoiceRepository{
    
    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getObject().getCurrentSession();
    }

    @Override
    public void createDetailInvoice(DetailInvoice detailInvoice) {
        getCurrentSession().persist(detailInvoice);
    }

    @Override
    public List<DetailInvoice> findAll() {
        return getCurrentSession()
                .createNamedQuery("DetailInvoice.findAll", DetailInvoice.class)
                .getResultList();
    }

    @Override
    public List<DetailInvoice> findByInvoiceId(Integer invoiceId) {
        return getCurrentSession()
                .createNamedQuery("DetailInvoice.findAll", DetailInvoice.class)
                .getResultList()
                .stream()
                .filter(detail -> detail.getInvoiceId().getId().equals(invoiceId))
                .toList();
    }
    
}
