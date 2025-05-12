/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repositories.impl;

import com.apartment_management.pojo.Response;
import com.apartment_management.repositories.ResponseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
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
public class ResponseRepositoryImpl implements ResponseRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void submitResponse(Response response) {
        this.getCurrentSession().persist(response);
    }

    @Override
    public List<Response> getResponses(Integer surveyId, Integer questionId, Integer userId) {
        Session session = this.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Response> cq = cb.createQuery(Response.class);
        Root root = cq.from(Response.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (surveyId != null) {
            predicates.add(cb.equal(root.get("questionId").get("survey").get("id"), surveyId));
        }

        if (questionId != null) {
            predicates.add(cb.equal(root.get("questionId").get("id"), questionId));
        }

        if (userId != null) {
            predicates.add(cb.equal(root.get("userId").get("id"), userId));
        }

        cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        return session.createQuery(cq).getResultList();
    }

}
