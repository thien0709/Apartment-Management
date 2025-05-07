/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repository.impl;

import com.apartment_management.pojo.Survey;
import com.apartment_management.repository.SurveyRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 *
 * @author thien
 */
public class SurveyRepositoryImpl implements SurveyRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<Survey> getAllSurveys() {
        Query q = this.getCurrentSession().createQuery("From Survey", Survey.class);
        return q.getResultList();
    }

    @Override
    public Survey getSurveyById(int id) {
        return this.getCurrentSession().find(Survey.class, id);
    }

    @Override
    public void createSurvey(Survey survey) {
        this.getCurrentSession().persist(survey);
    }

    @Override
    public void updateSurvey(Survey survey) {
        Survey existing = getSurveyById(survey.getId());
        if (existing != null) {
            this.getCurrentSession().merge(survey);
        }

    }

    @Override
    public void deleteSurvey(int id) {
        Survey s = getSurveyById(id);
        if (s != null) {
            this.getCurrentSession().remove(s);
        }
    }

}
