/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Question;
import com.apartment_management.pojo.Survey;
import com.apartment_management.repositories.QuestionRepository;
import com.apartment_management.repositories.SurveyRepository;
import com.apartment_management.services.QuestionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author thien
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    
    @Autowired
    private QuestionRepository questionRepo;
    
    @Autowired
    private SurveyRepository surveyRepo;
    
    @Override
    public List<Question> getQuestionsBySurveyId(int surveyId) {
        return this.questionRepo.getQuestionsBySurveyId(surveyId);
    }
    
    @Override
    public Question getQuestionById(int id) {
        return this.questionRepo.getQuestionById(id);
    }
    
    @Override
    public void addQuestion(String content, int surveyId) {
        Survey s = this.surveyRepo.getSurveyById(surveyId);
        Question q = new Question();
        q.setContent(content);
        q.setSurveyId(s);
        this.questionRepo.addQuestion(q);
    }
    
    @Override
    public void updateQuestion(int questionId, String content) {
        Question existing = this.questionRepo.getQuestionById(questionId);
        if (existing != null) {
            existing.setContent(content);
            this.questionRepo.updateQuestion(existing);
        }
    }
    
    @Override
    public void deleteQuestion(int id) {
        this.questionRepo.deleteQuestion(id);
    }
    
}
