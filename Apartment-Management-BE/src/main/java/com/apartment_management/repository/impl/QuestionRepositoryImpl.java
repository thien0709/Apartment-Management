/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repository.impl;

import com.apartment_management.pojo.Question;
import com.apartment_management.repository.QuestionRepository;
import jakarta.persistence.Query;
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
public class QuestionRepositoryImpl implements QuestionRepository {

    @Autowired
    LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<Question> getQuestionsBySurveyId(int surveyId) {
        Query q = this.getCurrentSession().createQuery("FROM Question s WHERE s.surveyId.id = :surveyId", Question.class);
        q.setParameter("surveyId", surveyId);
        return q.getResultList();
    }

    @Override
    public Question getQuestionById(int id) {
        return this.getCurrentSession().get(Question.class, id);
    }

    @Override
    public void addQuestion(Question question) {
        this.getCurrentSession().persist(question);
        }

    @Override
    public void updateQuestion(Question question) {
        this.getCurrentSession().merge(question);
      }

    @Override
    public void deleteQuestion(int id) {
        Question q = this.getQuestionById(id);
        this.getCurrentSession().remove(q);
    }

}
