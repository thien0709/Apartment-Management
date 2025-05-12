/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Question;
import com.apartment_management.pojo.Response;
import com.apartment_management.pojo.User;
import com.apartment_management.repositories.QuestionRepository;
import com.apartment_management.repositories.ResponseRepository;
import com.apartment_management.repositories.UserRepository;
import com.apartment_management.services.ResponseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author thien
 */
@Service
public class ResponseServiceImpl implements ResponseService{
    
    @Autowired
    private ResponseRepository responseRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private QuestionRepository questionRepo;

    @Override
    public void submitResponse(String answer, int questionId, int userId) {
        User u = this.userRepo.getUserById(userId);
        Question q = this.questionRepo.getQuestionById(questionId);
        Response r = new Response();
        r.setAnswer(answer);
        r.setQuestionId(q);
        r.setUserId(u);
        this.responseRepo.submitResponse(r);
       }

    @Override
    public List<Response> getResponses(Integer surveyId, Integer questionId, Integer userId) {
        return this.responseRepo.getResponses(surveyId, questionId, userId);
    }
    
}
