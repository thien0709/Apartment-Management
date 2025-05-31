/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repositories.impl;

import com.apartment_management.pojo.DetailInvoice;
import com.apartment_management.pojo.Feed;
import com.apartment_management.pojo.Invoice;
import com.apartment_management.repositories.InvoiceRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
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
public class InvoiceRepositoryImpl implements InvoiceRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getObject().getCurrentSession();
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        getCurrentSession().persist(invoice);
        return invoice;
    }

    @Override
    public List<Invoice> findAll() {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Invoice> cq = cb.createQuery(Invoice.class);
        Root<Invoice> root = cq.from(Invoice.class);

        // Điều kiện: userId IS NOT NULL
        cq.select(root).where(cb.isNotNull(root.get("userId")));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Invoice findById(Integer id) {
        return getCurrentSession()
                .createNamedQuery("Invoice.findById", Invoice.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void deleteInvoice(Integer id) {
        Session session = getCurrentSession();
        Invoice invoice = session.get(Invoice.class, id);
        if (invoice != null) {
            session.delete(invoice);
        }
    }

    @Override
    public List<Invoice> getInvoicesByUserId(int userId) {
        Query<Invoice> query = getCurrentSession().createQuery(
                "FROM Invoice i LEFT JOIN FETCH i.detailInvoiceSet d LEFT JOIN FETCH d.feedId WHERE i.userId.id = :userId",
                Invoice.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }

//    @Override
//    public boolean updateStatusToPaid(Invoice invoice, String method) {
//        if (invoice == null || invoice.getId() == null) {
//            return false;
//        }
//        System.out.println("Payment success");
//        invoice.setStatus("PAID");
//        invoice.setPaymentMethod(method);
//        getCurrentSession().merge(invoice);
//        
//        return true;
//
//    }
    @Override
    public boolean updatePaymentInfo(Invoice invoice, String method, String paymentProofUrl) {
        if (invoice == null || invoice.getId() == null) {
            return false;
        }
        invoice.setPaymentMethod(method);
        invoice.setStatus("PAID");
        if (paymentProofUrl != null) {
            invoice.setPaymentProof(paymentProofUrl);
        }

        getCurrentSession().merge(invoice);
        return true;
    }

}
